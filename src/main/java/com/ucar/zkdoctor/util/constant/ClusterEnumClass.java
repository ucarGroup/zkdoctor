package com.ucar.zkdoctor.util.constant;

/**
 * Description: 集群相关枚举类
 * Created on 2018/1/9 11:36
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ClusterEnumClass {

    /**
     * 集群状态枚举类
     */
    public enum ClusterStatusEnum {

        /**
         * 集群未监控状态
         */
        NOT_MONITORED(1, "未监控"),

        /**
         * 集群监控状态，且正常运行
         */
        RUNNING(2, "运行中"),

        /**
         * 集群已下线
         */
        OFFLINE(3, "已下线"),

        /**
         * 集群监控状态，但异常
         */
        EXCEPTION(4, "异常");

        private Integer status;
        private String desc;

        ClusterStatusEnum(Integer status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public Integer getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByStatus(int status) {
            for (ClusterStatusEnum statusEnum : ClusterStatusEnum.values()) {
                if (statusEnum.getStatus() == status) {
                    return statusEnum.getDesc();
                }
            }
            return "null";
        }

        public static ClusterStatusEnum getClusterStatusEnumByStatus(int status) {
            for (ClusterStatusEnum statusEnum : ClusterStatusEnum.values()) {
                if (statusEnum.getStatus() == status) {
                    return statusEnum;
                }
            }
            return null;
        }
    }

}
