-- ----------------------------
-- Table structure for `zk_client_log`
-- ----------------------------
DROP TABLE IF EXISTS `zk_client_log`;
CREATE TABLE `zk_client_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `instance_id` int(10) DEFAULT NULL COMMENT '实例id',
  `cluster_id` int(10) unsigned DEFAULT NULL COMMENT '集群id',
  `client_ip` varchar(255) DEFAULT NULL COMMENT '客户端ip',
  `client_port` int(10) DEFAULT NULL COMMENT '客户端端口',
  `sid` varchar(255) DEFAULT NULL COMMENT 'session id',
  `queued` bigint(20) DEFAULT NULL COMMENT '堆积请求数',
  `recved` bigint(20) DEFAULT NULL COMMENT '收包数',
  `sent` bigint(20) DEFAULT NULL COMMENT '发包数',
  `est` bigint(20) DEFAULT NULL COMMENT '连接时间戳',
  `to_time` int(10) DEFAULT NULL COMMENT '超时时间',
  `lcxid` varchar(255) DEFAULT NULL COMMENT '最后客户端请求id',
  `lzxid` varchar(255) DEFAULT NULL COMMENT '最后事务id（状态变更id）',
  `lresp` bigint(20) DEFAULT NULL COMMENT '最后响应时间戳',
  `llat` int(10) DEFAULT NULL COMMENT '最新延时',
  `minlat` int(10) DEFAULT NULL COMMENT '最小延时',
  `avglat` int(10) DEFAULT NULL COMMENT '平均延时',
  `maxlat` int(10) DEFAULT NULL COMMENT '最大延时',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_instance` (`instance_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_cluster` (`cluster_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='实例客户端连接信息历史记录表';

-- ----------------------------
-- Table structure for `zk_cluster_alarm_user`
-- ----------------------------
DROP TABLE IF EXISTS `zk_cluster_alarm_user`;
CREATE TABLE `zk_cluster_alarm_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cluster_id` int(10) unsigned DEFAULT NULL COMMENT '集群id',
  `user_id` int(10) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_cluster_user` (`cluster_id`,`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='集群报警用户表';

-- ----------------------------
-- Table structure for `zk_cluster_info`
-- ----------------------------
DROP TABLE IF EXISTS `zk_cluster_info`;
CREATE TABLE `zk_cluster_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '集群id',
  `cluster_name` varchar(255) DEFAULT NULL COMMENT '集群名称',
  `officer` varchar(255) DEFAULT NULL COMMENT '负责人用户名',
  `instance_number` int(5) DEFAULT NULL COMMENT '实例个数',
  `status` int(5) DEFAULT NULL COMMENT '状态,1:未监控,2:运行中,3:已下线,4:异常',
  `deploy_type` tinyint(5) DEFAULT NULL COMMENT '部署类型,1:集群模式,2:独立模式',
  `service_line` int(10) DEFAULT NULL COMMENT '集群所属业务线',
  `version` varchar(255) DEFAULT NULL COMMENT 'zk版本号',
  `intro` varchar(255) DEFAULT NULL COMMENT '集群描述',
  `create_time` datetime DEFAULT NULL COMMENT '集群创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '集群修改时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_cluster_name` (`cluster_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='集群详情表';

-- ----------------------------
-- Table structure for `zk_cluster_state`
-- ----------------------------
DROP TABLE IF EXISTS `zk_cluster_state`;
CREATE TABLE `zk_cluster_state` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cluster_id` int(10) unsigned DEFAULT NULL COMMENT '集群id',
  `instance_number` int(10) DEFAULT NULL COMMENT '实例数',
  `avg_latency_max` bigint(10) DEFAULT NULL COMMENT '同一时间集群中实例的平均延时的最大值,单位:ms',
  `max_latency_max` bigint(10) DEFAULT NULL COMMENT '同一时间集群中实例的最大延时的最大值,单位:ms',
  `min_latency_max` bigint(10) DEFAULT NULL COMMENT '同一时间集群中实例的最小延时的最大值,单位:ms',
  `received_total` bigint(20) DEFAULT NULL COMMENT '集群总收包数',
  `sent_total` bigint(20) DEFAULT NULL COMMENT '集群总发包数',
  `connection_total` int(10) DEFAULT NULL COMMENT '集群总连接数',
  `znode_count` int(10) DEFAULT NULL COMMENT '节点数',
  `watcher_total` int(10) DEFAULT NULL COMMENT '集群总watch数',
  `ephemerals` int(10) DEFAULT NULL COMMENT '临时节点数',
  `outstanding_total` bigint(10) DEFAULT NULL COMMENT '集群总堆积请求数',
  `approximate_data_size` bigint(10) DEFAULT NULL COMMENT '数据大小,单位:byte',
  `open_file_descriptor_count_total` bigint(10) DEFAULT NULL COMMENT '集群总打开的文件描述符数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_cluster_id` (`cluster_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='集群状态表';

-- ----------------------------
-- Table structure for `zk_cluster_state_log`
-- ----------------------------
DROP TABLE IF EXISTS `zk_cluster_state_log`;
CREATE TABLE `zk_cluster_state_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cluster_id` int(10) unsigned DEFAULT NULL COMMENT '集群id',
  `instance_number` int(10) DEFAULT NULL COMMENT '实例数',
  `avg_latency_max` int(10) DEFAULT NULL COMMENT '同一时间集群中实例的平均延时的最大值,单位:ms',
  `max_latency_max` int(10) DEFAULT NULL COMMENT '同一时间集群中实例的最大延时的最大值,单位:ms',
  `min_latency_max` int(10) DEFAULT NULL COMMENT '同一时间集群中实例的最小延时的最大值,单位:ms',
  `received_total` bigint(20) DEFAULT NULL COMMENT '集群总收包数',
  `sent_total` bigint(20) DEFAULT NULL COMMENT '集群总发包数',
  `connection_total` int(10) DEFAULT NULL COMMENT '集群总连接数',
  `znode_count` int(10) DEFAULT NULL COMMENT '节点数',
  `watcher_total` int(10) DEFAULT NULL COMMENT '集群总watch数',
  `ephemerals` int(10) DEFAULT NULL COMMENT '临时节点数',
  `outstanding_total` int(10) DEFAULT NULL COMMENT '集群总堆积请求数',
  `approximate_data_size` int(10) DEFAULT NULL COMMENT '数据大小,单位:byte',
  `open_file_descriptor_count_total` int(10) DEFAULT NULL COMMENT '集群总打开的文件描述符数量',
  `create_time` datetime DEFAULT NULL COMMENT '集群状态收集时间',
  PRIMARY KEY (`id`),
  KEY `idx_cluster_id_time` (`cluster_id`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='集群状态历史表';

-- ----------------------------
-- Table structure for `zk_instance_info`
-- ----------------------------
DROP TABLE IF EXISTS `zk_instance_info`;
CREATE TABLE `zk_instance_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '实例id',
  `cluster_id` int(10) unsigned DEFAULT NULL COMMENT '集群id',
  `machine_id` int(10) DEFAULT NULL COMMENT '机器id',
  `host` varchar(255) DEFAULT NULL COMMENT '实例ip',
  `port` int(10) DEFAULT NULL COMMENT '端口号',
  `connection_monitor` tinyint(5) DEFAULT NULL COMMENT '实例的连接信息是否进行收集,true：收集，false：不收集',
  `deploy_type` tinyint(5) DEFAULT NULL COMMENT '部署类型,1:集群模式,2:独立模式',
  `server_state_lag` tinyint(5) DEFAULT NULL COMMENT '实例角色,0:follower,1:leader,2:observer,3:standalone',
  `status` int(5) DEFAULT NULL COMMENT '实例状态,0:异常,1:正在运行,3:已下线,4:未运行',
  `create_time` datetime DEFAULT NULL COMMENT '实例创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '实例修改时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='实例详情表';

-- ----------------------------
-- Table structure for `zk_instance_state`
-- ----------------------------
DROP TABLE IF EXISTS `zk_instance_state`;
CREATE TABLE `zk_instance_state` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `instance_id` int(10) DEFAULT NULL COMMENT '实例id',
  `host_info` varchar(100) DEFAULT NULL COMMENT '实例host:port',
  `cluster_id` int(10) unsigned DEFAULT NULL COMMENT '集群id',
  `version` varchar(255) DEFAULT NULL COMMENT '版本信息',
  `leader_id` int(10) DEFAULT NULL COMMENT 'leader实例id',
  `avg_latency` bigint(10) DEFAULT NULL COMMENT '平均延时,单位:ms',
  `max_latency` bigint(10) DEFAULT NULL COMMENT '最大延时,单位:ms',
  `min_latency` bigint(10) DEFAULT NULL COMMENT '最小延时,单位:ms',
  `received` bigint(20) DEFAULT NULL COMMENT '收包数',
  `sent` bigint(20) DEFAULT NULL COMMENT '发包数',
  `curr_connections` int(10) DEFAULT NULL COMMENT '当前连接数',
  `curr_outstandings` bigint(10) DEFAULT NULL COMMENT '当前堆积请求数',
  `server_state_lag` tinyint(5) DEFAULT NULL COMMENT '实例角色,0:follower,1:leader,2:observer,3:standalone',
  `curr_znode_count` int(10) DEFAULT NULL COMMENT '当前节点数',
  `curr_watch_count` int(10) DEFAULT NULL COMMENT '当前watch数',
  `curr_ephemerals_count` int(10) DEFAULT NULL COMMENT '当前临时节点数',
  `approximate_data_size` bigint(10) DEFAULT NULL COMMENT '数据大小,单位:byte',
  `open_file_descriptor_count` bigint(10) DEFAULT NULL COMMENT '打开的文件描述符个数',
  `max_file_descriptor_count` bigint(10) DEFAULT NULL COMMENT '最大文件描述符个数',
  `followers` int(10) DEFAULT NULL COMMENT 'follower数量',
  `synced_followers` int(10) DEFAULT NULL COMMENT '同步的follower数量',
  `pending_syncs` int(10) DEFAULT NULL COMMENT '待同步的follow数量',
  `zxid` bigint(20) DEFAULT NULL COMMENT '最后的事务id',
  `run_ok` tinyint(4) DEFAULT NULL COMMENT '实例是否运行正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_instance_id` (`instance_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='实例状态表';

-- ----------------------------
-- Table structure for `zk_instance_state_log`
-- ----------------------------
DROP TABLE IF EXISTS `zk_instance_state_log`;
CREATE TABLE `zk_instance_state_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `instance_id` int(10) DEFAULT NULL COMMENT '实例id',
  `host_info` varchar(100) DEFAULT NULL COMMENT '实例host:port',
  `cluster_id` int(10) DEFAULT NULL COMMENT '集群id',
  `version` varchar(255) DEFAULT NULL COMMENT '版本信息',
  `leader_id` int(10) DEFAULT NULL COMMENT 'leader实例id',
  `avg_latency` int(10) DEFAULT NULL COMMENT '平均延时,单位:ms',
  `max_latency` int(10) DEFAULT NULL COMMENT '最大延时,单位:ms',
  `min_latency` int(10) DEFAULT NULL COMMENT '最小延时,单位:ms',
  `received` bigint(20) DEFAULT NULL COMMENT '收包数',
  `sent` bigint(20) DEFAULT NULL COMMENT '发包数',
  `connections` int(10) DEFAULT NULL COMMENT '连接数',
  `znode_count` int(10) DEFAULT NULL COMMENT '节点数',
  `watch_count` int(10) DEFAULT NULL COMMENT 'watch数',
  `ephemerals_count` int(10) DEFAULT NULL COMMENT '临时节点数',
  `outstandings` int(10) DEFAULT NULL COMMENT '堆积请求数',
  `approximate_data_size` int(10) DEFAULT NULL COMMENT '数据大小,单位:byte',
  `open_file_descriptor_count` int(10) DEFAULT NULL COMMENT '打开的文件描述符数量',
  `max_file_descriptor_count` int(10) DEFAULT NULL COMMENT '最大文件描述符数量',
  `followers` int(10) DEFAULT NULL COMMENT 'follower数量',
  `synced_followers` int(10) DEFAULT NULL COMMENT '同步的follower数',
  `pending_syncs` int(10) DEFAULT NULL COMMENT '待同步的follower数',
  `zxid` bigint(20) DEFAULT NULL COMMENT '最后的事务id',
  `run_ok` tinyint(4) DEFAULT NULL COMMENT '是否运行正常',
  `server_state_lag` tinyint(5) DEFAULT NULL COMMENT '实例角色,0:follower,1:leader,2:observer,3:standalone',
  `create_time` datetime DEFAULT NULL COMMENT '实例状态收集时间',
  PRIMARY KEY (`id`),
  KEY `idx_cluster` (`cluster_id`,`instance_id`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='实例状态历史信息表';

-- ----------------------------
-- Table structure for `zk_machine_info`
-- ----------------------------
DROP TABLE IF EXISTS `zk_machine_info`;
CREATE TABLE `zk_machine_info` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '机器id',
  `host` varchar(255) DEFAULT NULL COMMENT '机器ip',
  `memory` int(10) DEFAULT NULL COMMENT '内存大小,单位:GB',
  `cpu` int(10) DEFAULT NULL COMMENT 'cpu个数',
  `virtual` tinyint(4) DEFAULT NULL COMMENT '是否为虚机',
  `real_host` varchar(255) DEFAULT NULL COMMENT '如果是虚机,对应物理机ip',
  `room` varchar(255) DEFAULT NULL COMMENT '机器所在机房名称',
  `monitor` tinyint(4) DEFAULT NULL COMMENT '机器是否处于监控状态，true：监控中，false：未监控',
  `available` tinyint(4) DEFAULT NULL COMMENT '机器是否可用',
  `service_line` int(10) DEFAULT '0' COMMENT '机器所属业务线，默认不区分，为0：default',
  `host_name` varchar(255) DEFAULT NULL COMMENT '机器名',
  `host_domain` varchar(255) DEFAULT NULL COMMENT '域名',
  `create_time` datetime DEFAULT NULL COMMENT '机器创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '机器修改时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_host` (`host`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='机器详情表';

-- ----------------------------
-- Table structure for `zk_machine_state`
-- ----------------------------
DROP TABLE IF EXISTS `zk_machine_state`;
CREATE TABLE `zk_machine_state` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `machine_id` int(10) DEFAULT NULL COMMENT '机器id',
  `host` varchar(50) DEFAULT NULL COMMENT '机器ip',
  `cpu_usage` varchar(100) DEFAULT NULL COMMENT 'cpu使用率,百分制数',
  `avg_load` varchar(100) DEFAULT NULL COMMENT '机器负载',
  `net_traffic` varchar(100) DEFAULT NULL COMMENT 'io网络流量,单位:KB/s',
  `memory_usage` varchar(100) DEFAULT NULL COMMENT '内存使用率,百分制数',
  `memory_free` varchar(100) DEFAULT NULL COMMENT '空闲内存量,单位:kb',
  `memory_total` varchar(100) DEFAULT NULL COMMENT '总内存量,单位:kb',
  `disk_usage` varchar(100) DEFAULT NULL COMMENT '磁盘使用率,data目录所在磁盘使用率',
  `data_disk_used` varchar(100) DEFAULT NULL COMMENT 'dataDir下磁盘使用大小，单位：GB',
  `data_disk_total` varchar(100) DEFAULT NULL COMMENT 'dataDir下磁盘总大小，单位：GB',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_machine_id` (`machine_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='机器状态表';

-- ----------------------------
-- Table structure for `zk_machine_state_log`
-- ----------------------------
DROP TABLE IF EXISTS `zk_machine_state_log`;
CREATE TABLE `zk_machine_state_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `machine_id` int(10) DEFAULT NULL COMMENT '机器id',
  `host` varchar(50) DEFAULT NULL COMMENT '机器ip',
  `cpu_user_percent` varchar(100) DEFAULT NULL COMMENT '用户级别下消耗的CPU时间的比例',
  `cpu_nice_percent` varchar(100) DEFAULT NULL COMMENT 'nice优先级用户级应用程序消耗的CPU时间比例',
  `cpu_sys_percent` varchar(100) DEFAULT NULL COMMENT '系统核心级别下消耗的CPU时间的比例',
  `cpu_single_usage` varchar(500) DEFAULT NULL COMMENT '单cpu使用率情况,以空格分隔',
  `cpu_usage` varchar(100) DEFAULT NULL COMMENT 'cpu使用率,百分制数',
  `io_wait_percent` varchar(100) DEFAULT NULL COMMENT '等待I/O操作占用CPU总时间的百分比',
  `idle_percent` varchar(100) DEFAULT NULL COMMENT 'CPU空闲时间占用CPU总时间的百分比',
  `memory_free` varchar(100) DEFAULT NULL COMMENT '空闲内存总量，单位：kb',
  `memory_total` varchar(100) DEFAULT NULL COMMENT '已用内存总量，单位：kb',
  `buffers` varchar(100) DEFAULT NULL COMMENT 'buffers内存总量，单位：kb',
  `cached` varchar(100) DEFAULT NULL COMMENT 'cached内存总量，单位：kb',
  `memory_usage` varchar(100) DEFAULT NULL COMMENT '内存使用率,百分制数',
  `avg_load` varchar(100) DEFAULT NULL COMMENT '最近一分钟系统平均负载',
  `disk_situation` varchar(500) DEFAULT NULL COMMENT '磁盘使用情况，每个挂载点以-分隔，挂载点数据之间以空格分隔，记录已用空间大小、空闲空间大小，单位：GB',
  `disk_free_percent` varchar(500) DEFAULT NULL COMMENT '磁盘空闲率,每个挂载点以-分隔，挂载点数据之间以空格分隔，记录已用空间大小、空闲空间大小，单位：GB',
  `disk_usage` varchar(100) DEFAULT NULL COMMENT '磁盘使用率,data目录所在磁盘使用率',
  `net_traffic` varchar(100) DEFAULT NULL COMMENT '总流量，单位：KB/s',
  `net_flow_in` varchar(500) DEFAULT NULL COMMENT '入流量信息，取top10客户端ip以及其对应的入流量，单位：KB/s',
  `net_flow_out` varchar(500) DEFAULT NULL COMMENT '出流量信息，取top10客户端ip以及其对应的出流量，单位：KB/s',
  `create_time` datetime DEFAULT NULL COMMENT '收集时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`),
  KEY `idx_machine` (`machine_id`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='机器状态历史表';

-- ----------------------------
-- Table structure for `zk_monitor_indicator`
-- ----------------------------
DROP TABLE IF EXISTS `zk_monitor_indicator`;
CREATE TABLE `zk_monitor_indicator` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `indicator_name` varchar(255) DEFAULT NULL COMMENT '监控指标名称',
  `class_name` varchar(255) DEFAULT NULL COMMENT '监控指标实现的类名',
  `default_alert_value` varchar(255) DEFAULT NULL COMMENT '默认报警阀值',
  `default_alert_interval` int(10) DEFAULT NULL COMMENT '默认报警间隔，单位：分钟',
  `default_alert_frequency` int(10) DEFAULT '1' COMMENT '在报警间隔内，达到报警值的次数则进行报警',
  `default_alert_form` int(10) DEFAULT '0' COMMENT '默认报警形式，0：邮件，1：短信，2：邮件+短信',
  `alert_value_unit` varchar(255) DEFAULT NULL COMMENT '报警值单位',
  `switch_on` tinyint(1) DEFAULT NULL COMMENT '监控指标是否开启，0：关闭，1：开启',
  `modify_user_id` int(10) DEFAULT NULL COMMENT '修改该指标用户id',
  `info` varchar(200) DEFAULT NULL COMMENT '备注说明',
  `create_time` datetime DEFAULT NULL COMMENT '该指标创建时间',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `param1` varchar(50) DEFAULT NULL COMMENT '预留参数1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_class_name` (`class_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='监控指标表';

-- ----------------------------
-- Table structure for `zk_monitor_task`
-- ----------------------------
DROP TABLE IF EXISTS `zk_monitor_task`;
CREATE TABLE `zk_monitor_task` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `indicator_id` int(10) DEFAULT NULL COMMENT '监控指标id',
  `cluster_id` int(10) unsigned DEFAULT NULL COMMENT '集群id',
  `alert_value` varchar(255) DEFAULT NULL COMMENT '报警阀值',
  `alert_interval` int(10) DEFAULT NULL COMMENT '报警间隔，单位：分钟',
  `alert_frequency` int(10) DEFAULT '1' COMMENT '在报警间隔内，达到报警值的次数则进行报警',
  `alert_form` int(10) DEFAULT '0' COMMENT '报警形式，0：邮件，1：短信，2：邮件+短信',
  `switch_on` tinyint(1) DEFAULT NULL COMMENT '监控任务是否开启，0：关闭，1：开启',
  `modify_user_id` int(10) DEFAULT NULL COMMENT '修改人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `param1` varchar(50) DEFAULT NULL COMMENT '预留参数1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_cluster_indicator` (`indicator_id`,`cluster_id`) USING BTREE,
  KEY `idx_cluster_id` (`cluster_id`) USING BTREE,
  KEY `idx_indicator_id` (`indicator_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='监控任务表';

-- ----------------------------
-- Table structure for `zk_alarm_info`
-- ----------------------------
DROP TABLE IF EXISTS `zk_alarm_info`;
CREATE TABLE `zk_alarm_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `cluster_id` int(11) DEFAULT NULL COMMENT '集群id',
  `machine_id` int(11) DEFAULT NULL COMMENT '机器id',
  `machine_ip` varchar(255) DEFAULT NULL COMMENT '机器ip',
  `instance_id` int(11) DEFAULT NULL COMMENT '实例id',
  `instance_ip` varchar(255) DEFAULT NULL COMMENT '实例ip',
  `port` int(11) DEFAULT NULL COMMENT '端口号',
  `info_content` varchar(2000) DEFAULT NULL COMMENT '报警信息内容',
  `alarm_time` varchar(64) DEFAULT NULL COMMENT '报警时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='报警信息表';

-- ----------------------------
-- Table structure for `zk_qrtz_blob_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_blob_triggers`;
CREATE TABLE `zk_qrtz_blob_triggers` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `TRIGGER_NAME` varchar(100) NOT NULL COMMENT '触发器名',
  `TRIGGER_GROUP` varchar(100) NOT NULL COMMENT '触发器组名',
  `BLOB_DATA` blob COMMENT 'data',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz blob triggers';

-- ----------------------------
-- Table structure for `zk_qrtz_calendars`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_calendars`;
CREATE TABLE `zk_qrtz_calendars` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT 'scheduler名称',
  `CALENDAR_NAME` varchar(100) NOT NULL COMMENT 'calendar名称',
  `CALENDAR` blob COMMENT 'calendar信息',
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz calendars';

-- ----------------------------
-- Table structure for `zk_qrtz_cron_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_cron_triggers`;
CREATE TABLE `zk_qrtz_cron_triggers` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT 'scheduler名称',
  `TRIGGER_NAME` varchar(100) NOT NULL COMMENT 'trigger名',
  `TRIGGER_GROUP` varchar(100) NOT NULL COMMENT 'trigger组',
  `CRON_EXPRESSION` varchar(120) NOT NULL COMMENT 'cron表达式',
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz cron triggers';

-- ----------------------------
-- Table structure for `zk_qrtz_fired_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_fired_triggers`;
CREATE TABLE `zk_qrtz_fired_triggers` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `ENTRY_ID` varchar(95) NOT NULL COMMENT '条目id',
  `TRIGGER_NAME` varchar(100) NOT NULL COMMENT '出触发器名',
  `TRIGGER_GROUP` varchar(100) NOT NULL COMMENT '触发器组名',
  `INSTANCE_NAME` varchar(150) NOT NULL COMMENT '实例名',
  `FIRED_TIME` bigint(13) NOT NULL COMMENT '执行时间',
  `SCHED_TIME` bigint(13) NOT NULL COMMENT '调度时间',
  `PRIORITY` int(11) NOT NULL COMMENT '优先级',
  `STATE` varchar(16) NOT NULL COMMENT '状态',
  `JOB_NAME` varchar(150) DEFAULT NULL COMMENT 'job名',
  `JOB_GROUP` varchar(150) DEFAULT NULL COMMENT 'job组',
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL COMMENT '是否非并行执行',
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL COMMENT '是否持久化',
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz fired triggers';

-- ----------------------------
-- Table structure for `zk_qrtz_job_details`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_job_details`;
CREATE TABLE `zk_qrtz_job_details` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `JOB_NAME` varchar(100) NOT NULL COMMENT 'job名',
  `JOB_GROUP` varchar(100) NOT NULL COMMENT 'job组名',
  `DESCRIPTION` varchar(250) DEFAULT NULL COMMENT '描述',
  `JOB_CLASS_NAME` varchar(250) NOT NULL COMMENT 'job类名',
  `IS_DURABLE` varchar(1) NOT NULL COMMENT '是否持久化，0不持久化，1持久化',
  `IS_NONCONCURRENT` varchar(1) NOT NULL COMMENT '是否非并发，0非并发，1并发',
  `IS_UPDATE_DATA` varchar(1) NOT NULL COMMENT '是否更新数据',
  `REQUESTS_RECOVERY` varchar(1) NOT NULL COMMENT '是否可恢复，0不恢复，1恢复',
  `JOB_DATA` blob COMMENT 'job数据',
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz job details';

-- ----------------------------
-- Table structure for `zk_qrtz_locks`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_locks`;
CREATE TABLE `zk_qrtz_locks` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `LOCK_NAME` varchar(40) NOT NULL COMMENT '锁名',
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz locks';

-- ----------------------------
-- Table structure for `zk_qrtz_paused_trigger_grps`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_paused_trigger_grps`;
CREATE TABLE `zk_qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `TRIGGER_GROUP` varchar(100) NOT NULL COMMENT '触发器组',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz paused triggers';

-- ----------------------------
-- Table structure for `zk_qrtz_scheduler_state`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_scheduler_state`;
CREATE TABLE `zk_qrtz_scheduler_state` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `INSTANCE_NAME` varchar(100) NOT NULL COMMENT '执行quartz实例的主机名',
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL COMMENT '实例将状态报告给集群中的其它实例的上一次时间',
  `CHECKIN_INTERVAL` bigint(13) NOT NULL COMMENT '实例间状态报告的时间频率',
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz Scheduler state';

-- ----------------------------
-- Table structure for `zk_qrtz_simple_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_simple_triggers`;
CREATE TABLE `zk_qrtz_simple_triggers` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `TRIGGER_NAME` varchar(100) NOT NULL COMMENT '触发器名',
  `TRIGGER_GROUP` varchar(100) NOT NULL COMMENT '触发器组',
  `REPEAT_COUNT` bigint(7) NOT NULL COMMENT '重复次数',
  `REPEAT_INTERVAL` bigint(12) NOT NULL COMMENT '重复间隔',
  `TIMES_TRIGGERED` bigint(10) NOT NULL COMMENT '已触发次数',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz simple triggers';

-- ----------------------------
-- Table structure for `zk_qrtz_simprop_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_simprop_triggers`;
CREATE TABLE `zk_qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `TRIGGER_NAME` varchar(100) NOT NULL COMMENT '触发器名',
  `TRIGGER_GROUP` varchar(100) NOT NULL COMMENT '触发器组',
  `STR_PROP_1` varchar(512) DEFAULT NULL COMMENT 'str参数1',
  `STR_PROP_2` varchar(512) DEFAULT NULL COMMENT 'str参数2',
  `STR_PROP_3` varchar(512) DEFAULT NULL COMMENT 'str参数3',
  `INT_PROP_1` int(11) DEFAULT NULL COMMENT 'int参数1',
  `INT_PROP_2` int(11) DEFAULT NULL COMMENT 'int参数2',
  `LONG_PROP_1` bigint(20) DEFAULT NULL COMMENT 'long参数1',
  `LONG_PROP_2` bigint(20) DEFAULT NULL COMMENT 'long参数2',
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal参数1',
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal参数2',
  `BOOL_PROP_1` varchar(1) DEFAULT NULL COMMENT 'bool参数1',
  `BOOL_PROP_2` varchar(1) DEFAULT NULL COMMENT 'bool参数2',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz simple triggers';

-- ----------------------------
-- Table structure for `zk_qrtz_triggers`
-- ----------------------------
DROP TABLE IF EXISTS `zk_qrtz_triggers`;
CREATE TABLE `zk_qrtz_triggers` (
  `SCHED_NAME` varchar(50) NOT NULL COMMENT '调度名',
  `TRIGGER_NAME` varchar(100) NOT NULL COMMENT '触发器名',
  `TRIGGER_GROUP` varchar(100) NOT NULL COMMENT '触发器组',
  `JOB_NAME` varchar(150) NOT NULL COMMENT 'job名',
  `JOB_GROUP` varchar(150) NOT NULL COMMENT 'job组',
  `DESCRIPTION` varchar(250) DEFAULT NULL COMMENT '描述',
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL COMMENT '下次执行时间',
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL COMMENT '上次执行时间',
  `PRIORITY` int(11) DEFAULT NULL COMMENT '优先级',
  `TRIGGER_STATE` varchar(16) NOT NULL COMMENT '触发器状态',
  `TRIGGER_TYPE` varchar(8) NOT NULL COMMENT '触发器类型',
  `START_TIME` bigint(13) NOT NULL COMMENT '开始时间',
  `END_TIME` bigint(13) DEFAULT NULL COMMENT '结束时间',
  `CALENDAR_NAME` varchar(150) DEFAULT NULL COMMENT 'calendar名',
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL COMMENT 'misfire',
  `JOB_DATA` blob COMMENT 'job数据',
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Quartz triggers';

-- ----------------------------
-- Table structure for `zk_service_line`
-- ----------------------------
DROP TABLE IF EXISTS `zk_service_line`;
CREATE TABLE `zk_service_line` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `service_line_name` varchar(100) NOT NULL COMMENT '业务线名称',
  `service_line_desc` varchar(500) DEFAULT NULL COMMENT '业务线描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_idx_service_line` (`service_line_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='业务线表';

-- ----------------------------
-- Table structure for `zk_sys_config`
-- ----------------------------
DROP TABLE IF EXISTS `zk_sys_config`;
CREATE TABLE `zk_sys_config` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `config_name` varchar(255) DEFAULT NULL COMMENT '配置名称',
  `config_value` varchar(500) DEFAULT NULL COMMENT '配置值',
  `config_desc` varchar(255) DEFAULT '' COMMENT '描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_config_name` (`config_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='配置信息表';

-- ----------------------------
-- Table structure for `zk_user`
-- ----------------------------
DROP TABLE IF EXISTS `zk_user`;
CREATE TABLE `zk_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(255) NOT NULL COMMENT '用户名,可唯一识别用户',
  `password` varchar(255) DEFAULT NULL COMMENT '用户非ladp登录用户，单独存储用户密码。需密文存储',
  `ch_name` varchar(255) DEFAULT NULL COMMENT '中文名',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `user_role` tinyint(10) DEFAULT NULL COMMENT '用户角色,0:普通用户,1:管理员,2:超级管理员',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '用户信息修改时间',
  `param1` varchar(255) DEFAULT NULL COMMENT '预留参数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_name` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of zk_user, 用户名：admin，密码：admin
-- ----------------------------
INSERT INTO `zk_user` VALUES ('1', 'admin', '-272874517023827102959669599430568608046907545193', 'admin', null, null, '2', null, null, null);
