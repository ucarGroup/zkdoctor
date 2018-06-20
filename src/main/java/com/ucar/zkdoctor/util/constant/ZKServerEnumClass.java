package com.ucar.zkdoctor.util.constant;

/**
 * Description: zk服务相关枚举类
 * Created on 2018/1/9 11:45
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZKServerEnumClass {

    /**
     * 部署类型枚举类
     */
    public enum DeployTypeEnum {

        /**
         * 集群模式
         */
        CLUSTER(1, "集群"),

        /**
         * 独立模式
         */
        STANDALONE(2, "独立");

        private int deployType;
        private String desc;

        DeployTypeEnum(int deployType, String desc) {
            this.deployType = deployType;
            this.desc = desc;
        }

        public int getDeployType() {
            return deployType;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByDeployType(int deployType) {
            for (DeployTypeEnum deployTypeEnum : DeployTypeEnum.values()) {
                if (deployTypeEnum.getDeployType() == deployType) {
                    return deployTypeEnum.getDesc();
                }
            }
            return "null";
        }

        public static DeployTypeEnum getDeployTypeEnumByDeployType(int deployType) {
            for (DeployTypeEnum deployTypeEnum : DeployTypeEnum.values()) {
                if (deployTypeEnum.getDeployType() == deployType) {
                    return deployTypeEnum;
                }
            }
            return null;
        }
    }

    /**
     * zk角色枚举类
     */
    public enum ZKServerStateEnum {

        /**
         * follower
         */
        FOLLOWER(0, "follower"),

        /**
         * leader
         */
        LEADER(1, "leader"),

        /**
         * observer
         */
        OBSERVER(2, "observer"),

        /**
         * standalone
         */
        STANDALONE(3, "standalone");

        private int serverState;
        private String desc;

        ZKServerStateEnum(int serverState, String desc) {
            this.serverState = serverState;
            this.desc = desc;
        }

        public int getServerState() {
            return serverState;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByServerState(int serverState) {
            for (ZKServerStateEnum zkServerStateEnum : ZKServerStateEnum.values()) {
                if (zkServerStateEnum.getServerState() == serverState) {
                    return zkServerStateEnum.getDesc();
                }
            }
            return "null";
        }

        public static ZKServerStateEnum getServerStateEnumByServerState(int serverState) {
            for (ZKServerStateEnum zkServerStateEnum : ZKServerStateEnum.values()) {
                if (zkServerStateEnum.getServerState() == serverState) {
                    return zkServerStateEnum;
                }
            }
            return null;
        }

        public static Integer getServerStateByDesc(String desc) {
            for (ZKServerStateEnum zkServerStateEnum : ZKServerStateEnum.values()) {
                if (zkServerStateEnum.getDesc().equals(desc)) {
                    return zkServerStateEnum.getServerState();
                }
            }
            return null;
        }
    }
}
