package com.ucar.zkdoctor.util.constant;

/**
 * Description:
 * Created on 2018/1/9 11:38
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class InstanceEnumClass {

    /**
     * 实例状态枚举类
     */
    public enum InstanceStatusEnum {

        /**
         * 异常
         */
        EXCEPTION(0, "异常"),

        /**
         * 正在运行
         */
        RUNNING(1, "正在运行"),

        /**
         * 已下线
         */
        OFFLINE(3, "已下线"),

        /**
         * 未运行
         */
        NOT_RUNNING(4, "未运行");

        private int status;
        private String desc;

        InstanceStatusEnum(int status, String desc) {
            this.status = status;
            this.desc = desc;
        }

        public int getStatus() {
            return status;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByStatus(int status) {
            for (InstanceStatusEnum instanceStatusEnum : InstanceStatusEnum.values()) {
                if (instanceStatusEnum.getStatus() == status) {
                    return instanceStatusEnum.getDesc();
                }
            }
            return "null";
        }

        public static InstanceStatusEnum getInstanceStatusEnumByStatus(int status) {
            for (InstanceStatusEnum statusEnum : InstanceStatusEnum.values()) {
                if (statusEnum.getStatus() == status) {
                    return statusEnum;
                }
            }
            return null;
        }
    }
}
