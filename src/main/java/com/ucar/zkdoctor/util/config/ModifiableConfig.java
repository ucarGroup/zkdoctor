package com.ucar.zkdoctor.util.config;

import com.ucar.zkdoctor.util.constant.protocol.MachineProtocol;

/**
 * Description: 可修改的配置信息，修改后会保存到数据库中，以数据库中的为准，若未进行修改，则以默认值为准
 * Created on 2018/3/29 17:28
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ModifiableConfig {

    /**
     * ssh用户名、密码以及端口号
     */
    public static volatile String sshUserName = MachineProtocol.DEFAULT_SSH_USERNAME;
    public static volatile String sshPassword = MachineProtocol.DEFAULT_SSH_PASSWORD;
    public static volatile int sshPort = MachineProtocol.DEFAULT_SSH_PORT;
    /**
     * 定义上述配置值的描述。PS：private，名称以配置名+Desc结尾
     */
    private static String sshUserNameDesc = "ssh用户名";
    private static String sshPasswordDesc = "ssh密码";
    private static String sshPortDesc = "ssh端口号";

    /**
     * zookeeper服务安装目录相关
     */
    public static volatile String zkInstallDir = MachineProtocol.DEFAULT_ZK_INSTALL_DIR;
    public static volatile String zkConfDir = MachineProtocol.DEFAULT_ZK_CONF_DIR;
    public static volatile String zkDataDir = MachineProtocol.DEFAULT_ZK_DATA_DIR;
    /**
     * 定义上述配置值的描述。PS：private，名称以配置名+Desc结尾。ConfigService.initCongig()进行初始化加载该值
     */
    private static String zkInstallDirDesc = "zookeeper服务安装目录";
    private static String zkConfDirDesc = "zookeepr服务配置所在目录";
    private static String zkDataDirDesc = "zookeeper数据配置目录（dataDir）";

    /**
     * zkServer.sh目录
     */
    public static volatile String zkServer = MachineProtocol.DEFAULT_ZK_SERVER;
    /**
     * 定义上述配置值的描述。PS：private，名称以配置名+Desc结尾。ConfigService.initCongig()进行初始化加载该值
     */
    private static String zkServerDesc = "zookeeper zkServer.sh目录";

    /**
     * zookeeper安装文件下载链接
     */
    public static volatile String installFileDownloadSite = MachineProtocol.DEFAULT_INSTALL_FILE_DOWNLOAD_SITE;
    /**
     * 定义上述配置值的描述。PS：private，名称以配置名+Desc结尾。ConfigService.initCongig()进行初始化加载该值
     */
    private static String installFileDownloadSiteDesc = "zookeeper安装文件下载链接";

    /**
     * 采集服务器信息，一些命令安装目录
     */
    public static volatile String sarCommandDir = MachineProtocol.DEFAULT_SAR_COMMAND_DIR;
    public static volatile String dfCommandDir = MachineProtocol.DEFAULT_DF_COMMAND_DIR;
    public static volatile String iftopCommandDir = MachineProtocol.DEFAULT_IFTOP_COMMAND_DIR;
    /**
     * 定义上述配置值的描述。PS：private，名称以配置名+Desc结尾。ConfigService.initCongig()进行初始化加载该值
     */
    private static String sarCommandDirDesc = "采集服务器信息：sar命令安装目录";
    private static String dfCommandDirDesc = "采集服务器信息：df命令安装目录";
    private static String iftopCommandDirDesc = "采集服务器信息：iftop命令安装目录";

    /**
     * 上传升级jar文件的位置以及默认文件名称
     */
    public static volatile String uploadFileDir = MachineProtocol.DEFAULT_UPLOAD_FILE_DIR;
    public static volatile String uploadFileName = MachineProtocol.DEFAULT_UPLOAD_FILE_NAME;
    /**
     * 定义上述配置值的描述。PS：private，名称以配置名+Desc结尾。ConfigService.initCongig()进行初始化加载该值
     */
    private static String uploadFileDirDesc = "zookeeper升级jar：上传升级jar文件的位置";
    private static String uploadFileNameDesc = "zookeeper升级jar：上传升级jar文件名称";
}
