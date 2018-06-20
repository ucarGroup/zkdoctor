package com.ucar.zkdoctor.util.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import com.ucar.zkdoctor.pojo.bo.HostAndPort;
import com.ucar.zkdoctor.util.config.ModifiableConfig;
import com.ucar.zkdoctor.util.exception.SSHException;
import com.ucar.zkdoctor.util.thread.NamedThreadFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Description: ssh执行器
 * Created on 2018/1/23 12:37
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SSHExecutor {

    public static final Logger LOGGER = LoggerFactory.getLogger(SSHExecutor.class);

    /**
     * 连接timeout，单位：毫秒
     */
    private static final int CONNCET_TIMEOUT = 5000;

    /**
     * 操作timeout
     */
    private static final int OPERATE_TIMEOUT = 30000;

    /**
     * 回车换行
     */
    private static final String CRLF = "\r\n";

    /**
     * ssh操作线程池
     */
    private static ThreadPoolExecutor sshPool = new ThreadPoolExecutor(
            32, 128, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1000),
            new NamedThreadFactory("ssh", true));

    /**
     * ssh执行
     *
     * @param ip       目标服务器
     * @param callback 回调接口
     * @return
     * @throws SSHException
     */
    public Result execute(String ip, SSHCallback callback) throws SSHException {
        return execute(ip, ModifiableConfig.sshPort, ModifiableConfig.sshUserName, ModifiableConfig.sshPassword, callback);
    }

    /**
     * 执行命令
     *
     * @param ip       目标服务器ip
     * @param port     目标服务器port
     * @param username 用户名
     * @param password 密码
     * @param callback 回调接口
     * @throws SSHException
     */
    public Result execute(String ip, int port, String username, String password,
                          SSHCallback callback) throws SSHException {
        Connection conn = null;
        try {
            conn = getConnection(ip, port, username, password);
            return callback.call(new SSHSession(conn, HostAndPort.parseString(ip, port)));
        } catch (Exception e) {
            throw new SSHException("SSH error: " + e.getMessage(), e);
        } finally {
            close(conn);
        }
    }

    /**
     * 获取ssh连接
     *
     * @param ip       目标服务器ip
     * @param port     目标服务器port
     * @param username 用户名
     * @param password 密码
     * @throws Exception
     */
    private Connection getConnection(String ip, int port,
                                     String username, String password) throws Exception {
        Connection conn = new Connection(ip, port);
        conn.connect(null, CONNCET_TIMEOUT, CONNCET_TIMEOUT);
        boolean isAuthenticated = conn.authenticateWithPassword(username, password);
        if (!isAuthenticated) {
            throw new Exception("Get connection failed because SSH authentication failed, username:" + username + ", passsword:" + password);
        }
        return conn;
    }

    /**
     * SSH回调
     */
    public interface SSHCallback {
        /**
         * 具体命令执行方式接口
         *
         * @param session session
         * @return
         */
        Result call(SSHSession session);
    }

    /**
     * SSH session
     */
    public class SSHSession {

        /**
         * 目标服务器地址，ip:port格式
         */
        private String address;

        /**
         * 与目标服务器之间的连接
         */
        private Connection conn;

        private SSHSession(Connection conn, String address) {
            this.conn = conn;
            this.address = address;
        }

        /**
         * 执行命令
         */
        public Result executeCommand(String cmd) {
            return executeCommand(cmd, OPERATE_TIMEOUT);
        }

        /**
         * 执行命令
         */
        public Result executeCommand(String cmd, int timeoutMillis) {
            return executeCommand(cmd, null, timeoutMillis);
        }

        /**
         * 执行命令
         */
        public Result executeCommand(String cmd, LineProcessor lineProcessor, int timeoutMillis) {
            Session session = null;
            try {
                session = conn.openSession();
                return executeCommand(session, cmd, timeoutMillis, lineProcessor);
            } catch (Exception e) {
                LOGGER.error("SSH ip:{}, cmd:{}.", conn.getHostname(), cmd, e);
                return new Result(e);
            } finally {
                close(session);
            }
        }

        /**
         * 执行命令
         *
         * @param session       session
         * @param cmd           待执行命令
         * @param timeoutMillis 获取命令结果超时时长
         * @param lineProcessor 行处理器
         * @return
         * @throws Exception
         */
        public Result executeCommand(final Session session, final String cmd,
                                     final int timeoutMillis, final LineProcessor lineProcessor) throws Exception {
            Future<Result> future = sshPool.submit(new Callable<Result>() {
                public Result call() throws Exception {
                    // 执行命令
                    session.execCommand(cmd);
                    if (lineProcessor != null) {
                        processStream(session.getStdout(), lineProcessor);
                    } else {
                        String rst = getResult(session.getStdout());
                        if (rst != null) {
                            return new Result(true, rst);
                        }
                        // 获取可能异常信息
                        Result errResult = getStdError(session.getStderr(), cmd);
                        if (errResult != null) {
                            return errResult;
                        }
                    }
                    return new Result(true, null);
                }
            });
            Result rst = null;
            try {
                rst = future.get(timeoutMillis, TimeUnit.MILLISECONDS);
                future.cancel(true);
            } catch (TimeoutException e) {
                LOGGER.error("SSH ip:{}, cmd:{} timeoutMillis:{}", conn.getHostname(), cmd, timeoutMillis, e);
                throw new SSHException(e);
            }
            return rst;
        }

        /**
         * 错误日志记录
         *
         * @param is  输入流
         * @param cmd 命令
         * @return
         */
        private Result getStdError(InputStream is, String cmd) {
            String errInfo = getResult(is);
            if (errInfo != null) {
                LOGGER.error("SSH error info:{}, cmd:{}, address:{}. ", errInfo, cmd, address);
                return new Result(false, errInfo);
            }
            return null;
        }

        /**
         * 获取远程文件内容
         *
         * @param remoteFile 远程文件名称，包含路径
         * @return
         */
        public Result getRemoteFile(String remoteFile) {
            try {
                SCPClient client = conn.createSCPClient();
                OutputStream outputStream = new ByteArrayOutputStream();
                client.get(remoteFile, outputStream);
                return new Result(true, outputStream.toString());
            } catch (Exception e) {
                LOGGER.error("Get remoteFile {} error.", remoteFile, e);
                return new Result(e);
            }
        }

        /**
         * 写远程文件
         *
         * @param remoteDir 远程文件路径
         * @param fileName  远程文件名称
         * @param content   文件内容
         * @return
         */
        public Result writeRemoteFile(String remoteDir, String fileName, String content) {
            try {
                if (StringUtils.isNotBlank(content)) {
                    // window下的回车为\r\n，而linux为\n，需要进行替换
                    content = content.replace("\r\n", "\n");
                    SCPClient client = conn.createSCPClient();
                    // 创建的文件，用户拥有相关权限
                    client.put(content.getBytes(), fileName, remoteDir, "0644");
                    return new Result(true);
                }
            } catch (Exception e) {
                LOGGER.error("Write {} to remoteFile {}/{} error.", content, remoteDir, fileName, e);
                return new Result(e);
            }
            return new Result(false);
        }

        /**
         * 复制本地的文件至远程服务器
         *
         * @param remoteDir 远程服务器路径
         * @param localPath 本地文件路径
         * @return
         */
        public Result scpFileToRemote(String remoteDir, String localPath) {
            try {
                SCPClient scpClient = conn.createSCPClient();
                scpClient.put(localPath, remoteDir, "0644");
                return new Result(true);
            } catch (Exception e) {
                LOGGER.error("Scp file {} to remote dir {} error.", localPath, remoteDir, e);
                return new Result(e);
            }
        }

        /**
         * 创建目录
         *
         * @param remoteDir 待创建目录
         * @return
         */
        public Result mkDir(String remoteDir) {
            try {
                SFTPv3Client sftpClient = new SFTPv3Client(conn);
                // 权限全部给创建者
                sftpClient.mkdir(remoteDir, 0700);
                return new Result(true);
            } catch (Exception e) {
                LOGGER.error("Make remote dir {} failed.", remoteDir, e);
                return new Result(e);
            }
        }
    }

    /**
     * 获取SSH返回结果
     *
     * @param is 输入流
     * @return
     */
    private String getResult(InputStream is) {
        final StringBuffer buffer = new StringBuffer();
        processStream(is, new LineProcessor() {
            @Override
            public void process(String line, int lineNum) throws Exception {
                if (lineNum > 1) {
                    buffer.append(CRLF);
                }
                buffer.append(line);
            }
        });
        return buffer.length() > 0 ? buffer.toString() : null;
    }

    /**
     * 处理数据流
     *
     * @param is 输入流
     */
    private void processStream(InputStream is, LineProcessor lineProcessor) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new StreamGobbler(is)));
            String line = null;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                try {
                    lineProcessor.process(line, lineNum);
                } catch (Exception e) {
                    LOGGER.error("Process inputStream line:{} failed:", line, e);
                }
                lineNum++;
            }
        } catch (IOException e) {
            LOGGER.error("Process inputStream failed.", e);
        } finally {
            close(reader);
        }
    }

    /**
     * 解析数据并处理
     */
    public interface LineProcessor {
        /**
         * 解析每行数据
         *
         * @param line    每行内容
         * @param lineNum 行号
         * @throws Exception
         */
        void process(String line, int lineNum) throws Exception;
    }

    /**
     * ssh结果类
     */
    public class Result {

        /**
         * ssh是否成功
         */
        private boolean success;

        /**
         * 结果数据
         */
        private String result;

        /**
         * 异常
         */
        private Exception excetion;

        public Result(boolean success) {
            this.success = success;
        }

        public Result(boolean success, String result) {
            this.success = success;
            this.result = result;
        }

        public Result(Exception excetion) {
            this.success = false;
            this.excetion = excetion;
        }

        public Exception getExcetion() {
            return excetion;
        }

        public void setExcetion(Exception excetion) {
            this.excetion = excetion;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "Result{" +
                    "success=" + success +
                    ", result='" + result + '\'' +
                    ", excetion=" + excetion +
                    '}';
        }
    }

    /**
     * 关闭BufferedReader
     *
     * @param read BufferedReader
     */
    private void close(BufferedReader read) {
        if (read != null) {
            try {
                read.close();
            } catch (IOException e) {
                LOGGER.error("Close buffered reader failed.", e);
            }
        }
    }

    /**
     * 关闭connection
     *
     * @param conn 连接
     */
    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                LOGGER.error("Close ssh connection failed.", e);
            }
        }
    }

    /**
     * 关闭session
     *
     * @param session session
     */
    private static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (Exception e) {
                LOGGER.error("Close session failed.", e);
            }
        }
    }
}
