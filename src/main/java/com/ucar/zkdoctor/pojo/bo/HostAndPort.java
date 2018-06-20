package com.ucar.zkdoctor.pojo.bo;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: ip:port对象
 * Created on 2018/1/15 14:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class HostAndPort implements Serializable {

    private static final long serialVersionUID = 370485088768001879L;

    public static final String HOST_PORT_SEPARATOR = ":";

    public static final String NEXT_LINE = "\r\n";

    /**
     * ip
     */
    private String host;

    /**
     * port
     */
    private int port;

    /**
     * myid
     */
    private int myid;

    public HostAndPort(String host, int port, int myid) {
        this.host = host;
        this.port = port;
        this.myid = myid;
    }

    public HostAndPort(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * ip:port格式转化为HostAndPort对象
     *
     * @param hostAndPort ip:port对象
     * @return
     */
    public static HostAndPort transformToHostAndPort(String hostAndPort) {
        if (StringUtils.isBlank(hostAndPort)) {
            return null;
        }
        String[] hostAndPorts = hostAndPort.split(HOST_PORT_SEPARATOR);
        if (hostAndPorts.length == 2) {
            try {
                return new HostAndPort(hostAndPorts[0], Integer.parseInt(hostAndPorts[1]));
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 多行ip:port格式转化为HostAndPort list对象
     *
     * @param hostAndPortListStr 多行ip:port字符
     * @return
     */
    public static List<HostAndPort> transformToHostAndPortList(String hostAndPortListStr) {
        if (StringUtils.isBlank(hostAndPortListStr)) {
            return null;
        }
        String[] hostAndPorts = hostAndPortListStr.split(NEXT_LINE);
        List<HostAndPort> hostAndPortList = new ArrayList<HostAndPort>();
        for (String hostAndPort : hostAndPorts) {
            HostAndPort hp = transformToHostAndPort(hostAndPort);
            if (hp == null) {
                return null;
            } else {
                hostAndPortList.add(hp);
            }
        }
        return hostAndPortList;
    }

    /**
     * 根据server配置，提取服务器ip和port信息
     *
     * @param serverConfig 服务器配置，格式为:server.id=host:quorumPort:electionPort:peerType
     * @param port         端口
     * @return
     */
    public static List<HostAndPort> parseHostAndPortList(String serverConfig, Integer port) {
        if (StringUtils.isBlank(serverConfig) || port == null) {
            return null;
        }
        List<HostAndPort> hostAndPortList = new ArrayList<HostAndPort>();
        String[] serverConfigs = serverConfig.split(NEXT_LINE);
        for (String server : serverConfigs) {
            String myid = server.substring(server.indexOf(".") + 1, server.indexOf("="));
            String ip = server.substring(server.indexOf("=") + 1, server.indexOf(":"));
            if (StringUtils.isBlank(myid) || StringUtils.isBlank(ip)) {
                return null;
            }
            HostAndPort hostAndPort = new HostAndPort(ip, port, Integer.parseInt(myid));
            hostAndPortList.add(hostAndPort);
        }
        return hostAndPortList;
    }

    /**
     * 转化host:port格式
     *
     * @param host host
     * @param port port
     * @return
     */
    public static String parseString(String host, int port) {
        return host + HOST_PORT_SEPARATOR + port;
    }

    @Override
    public String toString() {
        return this.getHost() + HOST_PORT_SEPARATOR + this.getPort();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }
}
