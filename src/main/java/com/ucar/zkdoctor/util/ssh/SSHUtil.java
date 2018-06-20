package com.ucar.zkdoctor.util.ssh;

import com.ucar.zkdoctor.pojo.bo.CacheObject;
import com.ucar.zkdoctor.service.zk.impl.ZKServiceImpl;
import com.ucar.zkdoctor.util.config.ModifiableConfig;
import com.ucar.zkdoctor.util.constant.protocol.MachineProtocol;
import com.ucar.zkdoctor.util.constant.protocol.ZKProtocol;
import com.ucar.zkdoctor.util.exception.SSHException;
import com.ucar.zkdoctor.util.tool.DateUtil;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Description: 登录远程服务器，并发送shell命令
 * Created on 2018/1/23 12:35
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SSHUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SSHUtil.class);

    private final static SSHExecutor SSH_EXECUTOR = new SSHExecutor();

    /**
     * 下载文件到本地
     */

    private static final String WGET_COMMAND = "/usr/bin/wget -c -P %s %s";

    /**
     * 解压缩文件：待解压文件绝对路径  解压后文件夹绝对路径
     */
    private static final String DECOMPRESS_COMMAND = "/bin/tar -zxvf %s -C %s;mv %s/zookeeper*/* %s";

    /**
     * 备份文件，直接拷贝一份，原文件保留，参数：
     * 备份前文件路径
     * 备份前文件名称
     * 备份后文件路径
     * 备份后文件名称
     */
    private static final String BAK_FILE_COMMAND = "/bin/cp %s%s %s%s";

    /**
     * SSH 方式登录远程主机，执行命令
     *
     * @param host
     * @param command
     * @return
     * @throws SSHException
     */
    public static String execute(String host, String command) throws SSHException {
        return execute(host, ModifiableConfig.sshPort, ModifiableConfig.sshUserName,
                ModifiableConfig.sshPassword, command, true);
    }

    /**
     * SSH 方式登录远程主机，执行命令
     *
     * @param host
     * @param command
     * @param successReturn ssh成功才返回结果
     * @return
     * @throws SSHException
     */
    public static String execute(String host, String command, boolean successReturn) throws SSHException {
        return execute(host, ModifiableConfig.sshPort, ModifiableConfig.sshUserName,
                ModifiableConfig.sshPassword, command, successReturn);
    }

    /**
     * SSH 方式登录远程主机，执行命令
     *
     * @param host          远程主机ip
     * @param port          远程主机port
     * @param username      用户名
     * @param password      密码
     * @param command       命令
     * @param successReturn ssh成功才返回结果
     * @return
     * @throws SSHException
     */
    public static String execute(String host, int port, String username, String password,
                                 final String command, boolean successReturn) throws SSHException {
        if (StringUtils.isBlank(command)) {
            return StringUtils.EMPTY;
        }
        port = ModifiableConfig.sshPort;
        SSHExecutor.Result result = SSH_EXECUTOR.execute(host, port, username, password,
                new SSHExecutor.SSHCallback() {
                    public SSHExecutor.Result call(SSHExecutor.SSHSession session) {
                        return session.executeCommand(command);
                    }
                });
        // 要求执行成功才返回结果时，失败返回空
        if (result == null || successReturn && !result.isSuccess()) {
            return StringUtils.EMPTY;
        }
        return result.getResult();
    }


    /**
     * 确认zk进程是否在
     *
     * @param ip zk ip
     * @return
     * @throws SSHException
     */
    public static boolean isZKProcessRun(String ip) throws SSHException {
        String psCmd = "/bin/ps -ef | grep org.apache.zookeeper.server.quorum.QuorumPeerMain | grep -v grep";
        String psResponse = execute(ip, psCmd);
        boolean isRun = false;
        if (StringUtils.isNotBlank(psResponse)) {
            String[] resultArr = psResponse.split("\n");
            for (String resultLine : resultArr) {
                if (resultLine.contains(String.valueOf("zookeeper"))) {
                    isRun = true;
                    break;
                }
            }
        }
        return isRun;
    }

    /**
     * 获取机器状态信息，以map形式返回，key：命令名称，value：执行结果
     *
     * @param ip ip
     * @return
     */
    public static Map<String, String> collectMachineState(String ip) throws SSHException {
        return collectMachineState(ip, ModifiableConfig.sshPort,
                ModifiableConfig.sshUserName, ModifiableConfig.sshPassword);
    }

    /**
     * 获取机器状态信息，以map形式返回，key：命令名称，value：执行结果
     *
     * @param ip       ip
     * @param port     port
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static Map<String, String> collectMachineState(final String ip, int port, String userName,
                                                          String password) throws SSHException {
        Map<String, String> sshResults = new HashMap<String, String>();
        final StringBuffer sarResultSb = new StringBuffer();
        final StringBuffer dfResultSb = new StringBuffer();
        final StringBuffer iftopResultSb = new StringBuffer();
        SSH_EXECUTOR.execute(ip, port, userName, password,
                new SSHExecutor.SSHCallback() {
                    public SSHExecutor.Result call(SSHExecutor.SSHSession session) {
                        SSHExecutor.Result sar = session.executeCommand(MachineProtocol.getSarCommand());
                        if (sar != null && sar.isSuccess()) {
                            sarResultSb.append(sar.getResult());
                        }
                        SSHExecutor.Result df = session.executeCommand(MachineProtocol.getDfCommand());
                        if (df != null && df.isSuccess()) {
                            dfResultSb.append(df.getResult());
                        }
                        SSHExecutor.Result iftop = session.executeCommand(MachineProtocol.getIftopCommand());
                        if (iftop != null && iftop.isSuccess()) {
                            iftopResultSb.append(iftop.getResult());
                        }
                        return null;
                    }
                });
        sshResults.put(MachineProtocol.SAR, sarResultSb.toString());
        sshResults.put(MachineProtocol.DF, dfResultSb.toString());
        sshResults.put(MachineProtocol.IFTOP, iftopResultSb.toString());
        return sshResults;
    }

    /**
     * 获取zk配置文件内容
     *
     * @param ip zk ip
     * @return
     * @throws SSHException
     */
    public static String getZKConfig(String ip) throws SSHException {
        return getFileContent(ip, ZKProtocol.zkConfDir(), ModifiableConfig.sshPort,
                ModifiableConfig.sshUserName, ModifiableConfig.sshPassword);
    }

    /**
     * 获取远程文件内容
     *
     * @param ip       zk ip
     * @param path     远程文件路径（包含文件名称）
     * @param port     ssh port
     * @param userName 用户名
     * @param password 密码
     * @return
     * @throws SSHException
     */
    public static String getFileContent(String ip, final String path, int port, String userName, String password) throws SSHException {
        SSHExecutor.Result result = SSH_EXECUTOR.execute(ip, port, userName, password,
                new SSHExecutor.SSHCallback() {
                    public SSHExecutor.Result call(SSHExecutor.SSHSession session) {
                        return session.getRemoteFile(path);
                    }
                });
        if (!result.isSuccess()) {
            return null;
        }
        return result.getResult();
    }

    /**
     * 更新zk配置文件内容
     *
     * @param ip                 zk ip
     * @param zooConfFileContent 新配置文件内容
     * @return
     * @throws SSHException
     */
    public static boolean editZKConfig(String ip, String zooConfFileContent) throws SSHException {
        return editOrEditFile(ip, ModifiableConfig.zkConfDir, MachineProtocol.ZK_CONFIG_FILE, zooConfFileContent,
                ModifiableConfig.sshPort, ModifiableConfig.sshUserName, ModifiableConfig.sshPassword);
    }

    /**
     * 新增或修改配置
     *
     * @param ip                 zk ip
     * @param remoteDir          配置文件路径
     * @param fileName           配置文件名称
     * @param zooConfFileContent 新配置文件内容
     * @param port               ssh port
     * @param userName           用户名
     * @param password           密码
     * @return
     * @throws SSHException
     */
    public static boolean editOrEditFile(String ip, final String remoteDir, final String fileName, final String zooConfFileContent,
                                         int port, String userName, String password) throws SSHException {
        SSHExecutor.Result result = SSH_EXECUTOR.execute(ip, port, userName, password,
                new SSHExecutor.SSHCallback() {
                    public SSHExecutor.Result call(SSHExecutor.SSHSession session) {
                        return session.writeRemoteFile(remoteDir, fileName, zooConfFileContent);
                    }
                });
        return result.isSuccess();
    }

    /**
     * 增加新配置
     *
     * @param ip              目标服务器ip
     * @param dir             目录
     * @param confFileName    配置文件名称
     * @param confFileContent 配置文件内容
     * @return
     * @throws SSHException
     */
    public static boolean addNewConfigFile(String ip, String dir, String confFileName, String confFileContent) throws SSHException {
        return editOrEditFile(ip, dir, confFileName, confFileContent, ModifiableConfig.sshPort,
                ModifiableConfig.sshUserName, ModifiableConfig.sshPassword);
    }

    /**
     * 下载文件
     *
     * @param downloadPath 下载文件的目录
     * @param downloadSite 下载链接
     * @return
     */
    public static boolean downloadFile(String downloadPath, String downloadSite) {
        String command = String.format(WGET_COMMAND, downloadPath, downloadSite);
        int exitCode = -1;
        try {
            CommandLine cmdLine = CommandLine.parse(command);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setExitValue(0);
            ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 60 * 1000);
            executor.setWatchdog(watchdog);
            exitCode = executor.execute(cmdLine);
        } catch (ExecuteException e) {
            LOGGER.warn("Download file from {} to {} failed.", downloadSite, downloadPath, e);
        } catch (IOException e) {
            LOGGER.warn("Download file from {} to {} failed.", downloadSite, downloadPath, e);
        }
        return exitCode == 0;
    }

    /**
     * 部署zk服务
     *
     * @param ip                  目标服务器
     * @param remoteDir           远程服务器安装目录（注意：路径结尾没有/）
     * @param localServerFileName 本地安装文件目录以及文件名
     * @param installFileName     安装文件名称
     * @param confFileContent     配置文件内容
     * @param dataDir             dataDir配置
     * @param myIdContent         myid文件内容
     * @return
     * @throws SSHException
     */
    public static boolean installZKServer(String ip, String remoteDir, String localServerFileName, String installFileName, String confFileContent,
                                          String dataDir, String myIdContent) throws SSHException {
        return installZKServer(ip, ModifiableConfig.sshPort, ModifiableConfig.sshUserName, ModifiableConfig.sshPassword,
                remoteDir, localServerFileName, installFileName, confFileContent, dataDir, myIdContent);

    }

    /**
     * 部署zk服务
     *
     * @param ip                  目标服务器
     * @param port                ssh port
     * @param userName            用户名
     * @param password            密码
     * @param remoteDir           远程服务器安装目录（注意：路径结尾没有/）
     * @param localServerFileName 本地安装文件目录以及文件名
     * @param installFileName     安装文件名称
     * @param confFileContent     配置文件内容
     * @param dataDir             dataDir配置
     * @param myIdContent         myid文件内容
     * @return
     * @throws SSHException
     */
    public static boolean installZKServer(final String ip, int port, String userName, String password,
                                          final String remoteDir, final String localServerFileName, final String installFileName, final String confFileContent,
                                          final String dataDir, final String myIdContent) throws SSHException {
        SSHExecutor.Result result = SSH_EXECUTOR.execute(ip, port, userName, password,
                new SSHExecutor.SSHCallback() {
                    public SSHExecutor.Result call(SSHExecutor.SSHSession session) {
                        boolean operateResult;
                        // 对过程进行记录
                        CacheObject<List<String>> result = ZKServiceImpl.INSTALL_PEOCESS_MAP.get("install");
                        List<String> resultList = new LinkedList<String>();
                        if (result != null && result.getObject() != null) {
                            resultList = result.getObject();
                        }
                        resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：正在推送安装文件到目标服务器...");
                        // 推送指定安装文件到目标服务器指定目录
                        SSHExecutor.Result scpInstallFile = session.scpFileToRemote(remoteDir, localServerFileName);
                        operateResult = scpInstallFile != null && scpInstallFile.isSuccess();
                        if (!operateResult) {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "安装失败：推送安装文件到目标服务器失败");
                            return scpInstallFile;
                        } else {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：推送安装文件到目标服务器成功");
                        }

                        resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：正在解压安装文件...");
                        // 解压安装文件
                        String command = String.format(DECOMPRESS_COMMAND, remoteDir + MachineProtocol.PATH + installFileName, remoteDir, remoteDir, remoteDir);
                        SSHExecutor.Result decompress = session.executeCommand(command);
                        operateResult = decompress != null && decompress.isSuccess();
                        if (!operateResult) {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "安装失败：解压安装文件失败");
                            return decompress;
                        } else {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：解压安装文件成功");
                        }
                        // 推送指定配置zoo.cfg
                        try {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：正在推送zoo.cfg配置...");
                            operateResult = editZKConfig(ip, confFileContent);
                        } catch (SSHException e) {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "安装失败：推送zoo.cfg配置失败");
                            return SSH_EXECUTOR.new Result(false);
                        }

                        if (!operateResult) {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "安装失败：推送zoo.cfg配置失败");
                            return SSH_EXECUTOR.new Result(false);
                        } else {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：推送zoo.cfg配置成功");
                        }
                        resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：正在创建dataDir目录...");
                        SSHExecutor.Result mkDataDir = session.mkDir(dataDir);
                        operateResult = mkDataDir != null && mkDataDir.isSuccess();
                        if (!operateResult) {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "安装失败：创建dataDir目录失败");
                            return SSH_EXECUTOR.new Result(false);
                        } else {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：创建dataDir目录成功");
                        }
                        // 推送myid配置
                        try {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：正在推送myid配置...");
                            operateResult = addNewConfigFile(ip, ModifiableConfig.zkDataDir, "myid", myIdContent);
                        } catch (SSHException e) {
                            return SSH_EXECUTOR.new Result(false);
                        }
                        if (!operateResult) {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "安装失败：推送myid配置失败");
                            return SSH_EXECUTOR.new Result(false);
                        } else {
                            resultList.add(DateUtil.formatYYYYMMddHHMMss(new Date()) + "正在安装：创建dataDir目录成功");
                        }
                        return SSH_EXECUTOR.new Result(true);
                    }

                });
        return result != null && result.isSuccess();
    }

    /**
     * 上传升级jar包
     *
     * @param ip                  目标ip
     * @param remoteDir           远程服务器安装目录（注意：路径结尾没有/）
     * @param localServerFileName 本地安装文件目录以及文件名
     * @return
     * @throws SSHException
     */
    public static boolean uploadJarFile(String ip, String remoteDir, String localServerFileName) throws SSHException {
        return uploadJarFile(ip, ModifiableConfig.sshPort, ModifiableConfig.sshUserName, ModifiableConfig.sshPassword,
                remoteDir, localServerFileName);
    }

    /**
     * 上传升级jar包
     *
     * @param ip                  目标服务器
     * @param port                ssh port
     * @param userName            用户名
     * @param password            密码
     * @param remoteDir           远程服务器安装目录（注意：路径结尾没有/）
     * @param localServerFileName 本地安装文件目录以及文件名
     * @return
     * @throws SSHException
     */
    public static boolean uploadJarFile(final String ip, int port, String userName, String password,
                                        final String remoteDir, final String localServerFileName) throws SSHException {
        SSHExecutor.Result result = SSH_EXECUTOR.execute(ip, port, userName, password,
                new SSHExecutor.SSHCallback() {
                    public SSHExecutor.Result call(SSHExecutor.SSHSession session) {
                        return session.scpFileToRemote(remoteDir, localServerFileName);
                    }
                });
        return result != null && result.isSuccess();
    }

    /**
     * 备份文件
     *
     * @param ip             目标服务器
     * @param destDir        待备份文件所在文件夹
     * @param sourceFileName 待备份文件名称
     * @param newFileName    备份后文件名称
     * @return
     * @throws SSHException
     */
    public static boolean backUpFile(String ip, String destDir, String sourceFileName, String newFileName) throws SSHException {
        return backUpFile(ip, ModifiableConfig.sshPort, ModifiableConfig.sshUserName, ModifiableConfig.sshPassword,
                destDir, sourceFileName, newFileName);
    }

    /**
     * 备份文件
     *
     * @param ip             目标服务器
     * @param port           ssh port
     * @param userName       用户名
     * @param password       密码
     * @param destDir        待备份文件所在文件夹
     * @param sourceFileName 待备份文件名称
     * @param newFileName    备份后文件名称
     * @return
     * @throws SSHException
     */
    public static boolean backUpFile(final String ip, int port, String userName, String password,
                                     final String destDir, final String sourceFileName, final String newFileName) throws SSHException {
        SSHExecutor.Result result = SSH_EXECUTOR.execute(ip, port, userName, password,
                new SSHExecutor.SSHCallback() {
                    public SSHExecutor.Result call(SSHExecutor.SSHSession session) {
                        String command = String.format(BAK_FILE_COMMAND, destDir, sourceFileName, destDir, newFileName);
                        return session.executeCommand(command);
                    }
                });
        return result != null && result.isSuccess();
    }
}
