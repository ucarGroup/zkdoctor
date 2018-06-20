package com.ucar.zkdoctor.util.config;

import com.ucar.zkdoctor.util.constant.UserEnumClass.LoginCheckType;
import com.ucar.zkdoctor.util.constant.protocol.MachineProtocol;
import com.ucar.zkdoctor.util.parser.PropertiesReader;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Description: 配置相关工具类
 * Created on 2018/1/23 13:55
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ConfigUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

    /**
     * 基本配置文件名称
     */
    private static final String CONFIG_FILE_NAME = "zkdoctorConfig";

    /**
     * 数据默认保存时长
     */
    private static final int DEFAULT_KEEP_DAYS = 3;

    /**
     * 集群历史状态数据保存天数配置名称
     */
    private static final String CLUSTER_STATE_LOG_KEEPDAYS = "cluster_state_log_keepdays";

    /**
     * 实例历史状态数据保存天数配置名称
     */
    private static final String INSTANCE_STATE_LOG_KEEPDAYS = "instance_state_log_keepdays";

    /**
     * 机器历史状态数据保存天数配置名称
     */
    private static final String MACHINE_STATE_LOG_KEEPDAYS = "machine_state_log_keepdays";

    /**
     * 实例连接信息历史状态数据保存天数配置名称
     */
    private static final String CLIENT_CONNECTIONS_KEEPDAYS = "client_connections_keepdays";

    /**
     * 数据保存磁盘位置配置名称
     */
    private static final String ZK_DATA_DIR = "zk_data_dir";

    /**
     * 当前环境类型，测试：1，预生产：2，生产：3
     */
    private static final String ENV_TYPE = "envType";

    /**
     * 各个环境的访问地址
     */
    private static final String ENV_URL = "envURL";

    /**
     * 默认数据保存磁盘位置，用于监控磁盘使用率
     */
    private static final String DEFAULT_DATA_DIR = MachineProtocol.DISK_USR_DIR;

    /**
     * 集群历史状态数据保存天数
     */
    private static int clusterStateLogKeepDays = DEFAULT_KEEP_DAYS;

    /**
     * 实例历史状态数据保存天数
     */
    private static int instanceStateLogKeepDays = DEFAULT_KEEP_DAYS;

    /**
     * 机器历史状态数据保存天数
     */
    private static int machineStateLogKeepDays = DEFAULT_KEEP_DAYS;

    /**
     * 实例连接信息历史状态数据保存天数
     */
    private static int clientConnectionsKeepDays = DEFAULT_KEEP_DAYS;

    /**
     * 数据保存磁盘位置，用于监控磁盘使用率
     */
    private static String dataDirConfig = DEFAULT_DATA_DIR;

    /**
     * 当前部署环境描述
     */
    private static String envDesc = "";

    /**
     * 项目访问地址
     */
    private static String envURL = "";

    /**
     * 用户登录校验模式配置名称
     */
    private static final String LOGIN_CHECK_TYPE = "loginCheckType";

    /**
     * 用户登录校验模式，register：注册用户登录，ldap：邮箱校验
     */
    private static LoginCheckType loginCheckType = LoginCheckType.REGISTER;

    static {
        Properties config = PropertiesReader.getProperties(CONFIG_FILE_NAME);
        if (config == null) {
            LOGGER.error("config file {}.properties not found!", CONFIG_FILE_NAME);
        } else {
            String keeyDays = config.getProperty(CLUSTER_STATE_LOG_KEEPDAYS);
            if (StringUtils.isNotBlank(keeyDays)) {
                try {
                    clusterStateLogKeepDays = Integer.parseInt(keeyDays);
                } catch (NumberFormatException e) {
                    LOGGER.warn("CLUSTER_STATE_LOG_KEEPDAYS value must be a positive integer!", e);
                }
            }
            keeyDays = config.getProperty(INSTANCE_STATE_LOG_KEEPDAYS);
            if (StringUtils.isNotBlank(keeyDays)) {
                try {
                    instanceStateLogKeepDays = Integer.parseInt(keeyDays);
                } catch (NumberFormatException e) {
                    LOGGER.warn("INSTANCE_STATE_LOG_KEEPDAYS value must be a positive integer!", e);
                }
            }
            keeyDays = config.getProperty(MACHINE_STATE_LOG_KEEPDAYS);
            if (StringUtils.isNotBlank(keeyDays)) {
                try {
                    machineStateLogKeepDays = Integer.parseInt(keeyDays);
                } catch (NumberFormatException e) {
                    LOGGER.warn("MACHINE_STATE_LOG_KEEPDAYS value must be a positive integer!", e);
                }
            }

            keeyDays = config.getProperty(CLIENT_CONNECTIONS_KEEPDAYS);
            if (StringUtils.isNotBlank(keeyDays)) {
                try {
                    clientConnectionsKeepDays = Integer.parseInt(keeyDays);
                } catch (NumberFormatException e) {
                    LOGGER.warn("CLIENT_CONNECTIONS_KEEPDAYS value must be a positive integer!", e);
                }
            }

            String dataDir = config.getProperty(ZK_DATA_DIR);
            if (StringUtils.isNotBlank(dataDir)) {
                dataDirConfig = dataDir;
            }

            String envType = config.getProperty(ENV_TYPE);
            if (StringUtils.isNotBlank(envType)) {
                if (envType.equals("1")) {
                    envDesc = "[测试环境]";
                } else if (envType.equals("2")) {
                    envDesc = "[预生产环]";
                } else if (envType.equals("3")) {
                    envDesc = "[生产环境]";
                } else {
                    envDesc = "[测试环境]";
                }
            }

            String currentEnvURL = config.getProperty(ENV_URL);
            if (StringUtils.isNotBlank(currentEnvURL)) {
                envURL = currentEnvURL;
            }

            String loginType = config.getProperty(LOGIN_CHECK_TYPE);
            if (StringUtils.isNotBlank(loginType)) {
                loginCheckType = LoginCheckType.getLoginCheckTypeEnumByType(loginType);
            }
        }
    }

    public static int getClusterStateLogKeepDays() {
        return clusterStateLogKeepDays;
    }

    public static int getInstanceStateLogKeepDays() {
        return instanceStateLogKeepDays;
    }

    public static int getMachineStateLogKeepDays() {
        return machineStateLogKeepDays;
    }

    public static int getClientConnectionsKeepDays() {
        return clientConnectionsKeepDays;
    }

    public static String getDataDirConfig() {
        return dataDirConfig;
    }

    public static String getEnvDesc() {
        return envDesc;
    }

    public static String getEnvURL() {
        return envURL;
    }

    public static LoginCheckType getLoginCheckType() {
        return loginCheckType;
    }
}
