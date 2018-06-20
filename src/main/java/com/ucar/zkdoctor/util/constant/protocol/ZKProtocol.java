package com.ucar.zkdoctor.util.constant.protocol;

import com.ucar.zkdoctor.util.config.ModifiableConfig;
import org.apache.commons.lang.StringUtils;

/**
 * Description: zk相关运维命令
 * Created on 2018/1/23 12:20
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZKProtocol {

    /**
     * 启动zk服务的shell命令，固定配置文件：${zk_path}/conf/zoo.cfg
     *
     * @return
     */
    public static String startZKShell() {
        String start = "source /etc/profile && " + getZkServerCommand() + " start %s";
        return String.format(start, zkConfDir());
    }

    /**
     * 启动zk服务的shell命令，可指定配置文件，配置文件位置为confFilePath，若未指定，则不指定配置文件
     *
     * @param confFilePath 配置文件位置，包括路径以及配置文件名
     * @return
     */
    public static String startZKShell(String confFilePath) {
        String currConfFilePath = confFilePath == null ? StringUtils.EMPTY : confFilePath;

        String start = "source /etc/profile && " + getZkServerCommand() + " start %s";
        return String.format(start, currConfFilePath);
    }

    /**
     * 停止zk服务的shell命令
     *
     * @return
     */
    public static String stopZKShell() {
        return "source /etc/profile && " + getZkServerCommand() + "  stop";
    }

    /**
     * 集群status shell命令
     *
     * @return
     */
    public static String statusZKShell() {
        return "source /etc/profile && " + getZkServerCommand() + " status";
    }

    /**
     * 集群重启shell命令
     *
     * @return
     */
    public static String restartZKShell() {
        return "source /etc/profile && " + getZkServerCommand() + " restart";
    }

    /**
     * 根据zk版本信息获取当前zk的配置目录
     *
     * @return
     */
    public static String zkConfDir() {
        return ModifiableConfig.zkConfDir + MachineProtocol.PATH + MachineProtocol.ZK_CONFIG_FILE;
    }

    /**
     * 根据安装目录获取当前zk的配置目录
     *
     * @param installDir zk服务安装目录
     * @return
     */
    public static String zkConfDir(String installDir) {
        return installDir + MachineProtocol.CONF_FILE;
    }

    /**
     * 获取当前zkServer.sh命令信息
     *
     * @return
     */
    public static String getZkServerCommand() {
        // 检验当前配置的目录信息，保证目录分隔符“/”的位置
        if (ModifiableConfig.zkInstallDir.endsWith(MachineProtocol.PATH) && ModifiableConfig.zkServer.startsWith(MachineProtocol.PATH)) {
            return ModifiableConfig.zkInstallDir + ModifiableConfig.zkServer.substring(1, ModifiableConfig.zkServer.length());
        } else if (!ModifiableConfig.zkInstallDir.endsWith(MachineProtocol.PATH) && !ModifiableConfig.zkServer.startsWith(MachineProtocol.PATH)) {
            return ModifiableConfig.zkInstallDir + MachineProtocol.PATH + ModifiableConfig.zkServer;
        } else {
            return ModifiableConfig.zkInstallDir + ModifiableConfig.zkServer;
        }
    }

}
