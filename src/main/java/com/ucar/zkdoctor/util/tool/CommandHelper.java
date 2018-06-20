package com.ucar.zkdoctor.util.tool;

import com.ucar.zkdoctor.util.constant.FourLetterCommand;
import com.ucar.zkdoctor.util.parser.ReaderParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Description: 执行四字命令
 * Created on 2018/1/23 11:23
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CommandHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHelper.class);

    private static final int DEFAULT_TIME = 5000;

    public static <T> T doCommand(String host, int port, FourLetterCommand command, ReaderParser<T> parser) {
        return doCommand(host, port, command.name(), parser);
    }

    public static <T> T doCommand(String host, int port, String command, ReaderParser<T> parser) {
        if (command == null) {
            return null;
        }
        Socket socket = null;
        OutputStream outstream = null;
        BufferedReader reader = null;
        try {
            socket = new Socket();
            socket.setSoTimeout(DEFAULT_TIME);
            InetSocketAddress hostaddress = host != null ? new InetSocketAddress(host, port) :
                    new InetSocketAddress(InetAddress.getByName(null), port);
            socket.connect(hostaddress, DEFAULT_TIME);
            outstream = socket.getOutputStream();
            outstream.write(command.getBytes());
            outstream.flush();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            return parser.parseReader(reader);
        } catch (Exception e) {
            throw new RuntimeException("执行命令异常:" + host + "," + port + "," + command, e);
        } finally {
            if (outstream != null) {
                try {
                    outstream.close();
                } catch (IOException e) {
                    LOGGER.error("关闭流异常", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("关闭reader异常", e);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    LOGGER.error("关闭socket异常", e);
                }
            }
        }
    }
}
