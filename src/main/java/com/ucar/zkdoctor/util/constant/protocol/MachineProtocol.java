package com.ucar.zkdoctor.util.constant.protocol;

import com.ucar.zkdoctor.util.config.ModifiableConfig;

/**
 * Description: 机器相关常量
 * Created on 2018/1/23 12:21
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class MachineProtocol {

    /**
     * 默认：ssh机器相关用户名密码，实际调用参考：ModifiableConfig
     */
    public static final String DEFAULT_SSH_USERNAME = "zkdoctor";
    public static final String DEFAULT_SSH_PASSWORD = "zkdoctor";
    public static final int DEFAULT_SSH_PORT = 22;

    /**
     * 默认：zookeeper服务安装目录相关，实际调用参考：ModifiableConfig
     */
    public static final String DEFAULT_ZK_INSTALL_DIR = "/usr/local/zookeeper/";
    public static final String DEFAULT_ZK_CONF_DIR = "/usr/local/zookeeper/conf";
    public static final String DEFAULT_ZK_DATA_DIR = "/usr/local/zookeeper/data/";

    /**
     * 默认：zkServer.sh目录，实际调用参考：ModifiableConfig
     */
    public static final String DEFAULT_ZK_SERVER = "bin/zkServer.sh";

    /**
     * 默认：安装文件下载链接，实际调用参考：ModifiableConfig
     */
    public static final String DEFAULT_INSTALL_FILE_DOWNLOAD_SITE = "http://mirrors.shuosc.org/apache/zookeeper/zookeeper-3.4.10/zookeeper-3.4.10.tar.gz";

    /**
     * 默认：采集服务器信息，一些命令安装目录默认值，实际调用参考：ModifiableConfig
     */
    public static final String DEFAULT_SAR_COMMAND_DIR = "/usr/bin/sar";
    public static final String DEFAULT_DF_COMMAND_DIR = "/bin/df";
    public static final String DEFAULT_IFTOP_COMMAND_DIR = "/usr/sbin/iftop";

    /**
     * 默认：上传升级jar文件的位置以及默认文件名称，实际调用参考：ModifiableConfig
     */
    public static final String DEFAULT_UPLOAD_FILE_DIR = "/usr/local/zkdoctor/";
    public static final String DEFAULT_UPLOAD_FILE_NAME = "zookeeper-3.4.10.jar";

    /**
     * 目录分隔符
     */
    public static final String PATH = "/";

    /**
     * zk配置文件名称
     */
    public static final String ZK_CONFIG_FILE = "zoo.cfg";

    /**
     * 配置文件目录
     */
    public static final String CONF_FILE = "/conf/zoo.cfg";

    /**
     * 机器磁盘目录
     */
    public static final String DISK_USR_DIR = "/usr";

    /**
     * 采集机器信息的命令
     */
    public static final String SAR = "sar";
    public static final String DF = "df";
    public static final String IFTOP = "iftop";

    /**
     * 机器状态相关常量
     */
    public static final String CPU_USER_STRING = "user";
    public static final String MEM_USED_STRING = "memused";
    public static final String LDAVG_STRING = "ldavg-1";

    /**
     * 机器磁盘容量单位
     */
    public static final String SIZE_G = "G";
    public static final String SIZE_M = "M";

    /**
     * 流量单位
     */
    public static final String SIZE_GB = "GB";
    public static final String SIZE_MB = "MB";
    public static final String SIZE_KB = "KB";
    public static final String SIZE_B = "B";

    /**
     * 解析流量相关数据字符
     */
    public static final String DASH_SEPERATOR = "---";
    public static final String EQUAL_SEPERATOR = "===";
    public static final String TRAFFIC_SEND = "=>";
    public static final String TRAFFIC_RECEIVE = "<=";
    public static final String TOTAL_SEND = "Total send rate";
    public static final String TOTAL_RECEIVE = "Total receive rate";
    public static final String TOTAL_SEND_RECEIVE = "Total send and receive rate";

    /**
     * sar
     * -u -P ALL：CPU使用情况，统计到单核
     * -r：内存使用情况
     * -q：系统负载
     */
    public static String getSarCommand() {
        return ModifiableConfig.sarCommandDir + " -u -P ALL -q -r 1 3 | grep 'Average'";
    }

    /**
     * df
     */
    public static String getDfCommand() {
        return ModifiableConfig.dfCommandDir + " -lh";
    }

    /**
     * iftop。前提：机器安装iftop，且SSH用户有执行iftop的权限
     * -B，单位为byte，B，KB，MB，GB
     * -n 不转化hostname
     * -t 文本显示结果
     * -s n 等待n秒返回结果
     * -L n 显示n行结果，取top 10
     */
    public static String getIftopCommand() {
        return ModifiableConfig.iftopCommandDir + " -B -n -t -s 3 -L 10";
    }
}
