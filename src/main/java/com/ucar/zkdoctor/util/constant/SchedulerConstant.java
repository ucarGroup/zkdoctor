package com.ucar.zkdoctor.util.constant;

/**
 * Description:
 * Created on 2018/1/29 16:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SchedulerConstant {

    /**
     * zk信息收集定时任务相关信息
     */
    public static final String ZK_COLLECT_JOB_NAME = "zkJob";
    public static final String ZK_COLLECT_JOB_GROUP = "zk";
    public static final String ZK_COLLECT_TRIGGER_GROUP = "zookeeper-";
    public static final String ZK_COLLECT_TRIGGER_NAME = "zkStateTrigger";
    public static final String ZK_COLLECT_JOB_CRON = "10 0/1 * ? * *";
    public static final String CLUSTER_KEY = "cluster_key";
    public static final String ZK_COLLECT_JOB_DESC = "收集zk信息,每个集群对应一个定时任务";

    /**
     * 历史数据清除定时任务相关信息
     */
    public static final String ZK_CLEAN_JOB_NAME = "cleanJob";
    public static final String ZK_CLEAN_JOB_GROUP = "clean";
    public static final String ZK_CLEAN_TRIGGER_GROUP = "clean";
    public static final String ZK_CLEAN_TRIGGER_NAME = "cleanTrigger";
    public static final String ZK_CLEAN_JOB_CRON = "0 0 1 ? * *";
    public static final String ZK_CLEAN_JOB_DESC = "统计数据清除定时任务";

    /**
     * 监控定时任务相关信息
     */
    public static final String ZK_MONITOR_JOB_NAME = "monitorJob";
    public static final String ZK_MONITOR_JOB_GROUP = "monitor";
    public static final String ZK_MONITOR_TRIGGER_GROUP = "monitor";
    public static final String ZK_MONITOR_TRIGGER_NAME = "monitorTrigger";
    public static final String ZK_MONITOR_JOB_CRON = "40 0/1 * ? * *";
    public static final String ZK_MONITOR_JOB_DESC = "监控报警定时任务";

    /**
     * 机器信息收集定时任务相关信息
     */
    public static final String MACHINE_COLLECT_JOB_NAME = "machineJob";
    public static final String MACHINE_COLLECT_JOB_GROUP = "machine";
    public static final String MACHINE_COLLECT_TRIGGER_GROUP = "machine";
    public static final String MACHINE_COLLECT_TRIGGER_NAME = "machineTrigger";
    public static final String MACHINE_COLLECT_JOB_CRON = "20 0/1 * ? * *";
    public static final String MACHINE_COLLECT_JOB_DESC = "机器信息收集定时任务";

    /**
     * 实例连接信息收集定时任务相关信息
     */
    public static final String INSTANCE_CONN_COLLECT_JOB_NAME = "instanceConnCollectJob";
    public static final String INSTANCE_CONN_COLLECT_JOB_GROUP = "instanceConnCollect";
    public static final String INSTANCE_CONN_COLLECT_TRIGGER_GROUP = "instanceConnCollect";
    public static final String INSTANCE_CONN_COLLECT_TRIGGER_NAME = "instanceConnCollectTrigger";
    public static final String INSTANCE_CONN_COLLECT_JOB_CRON = "30 0/1 * ? * *";
    public static final String INSTANCE_CONN_COLLECT_JOB_DESC = "实例连接信息收集定时任务";

}
