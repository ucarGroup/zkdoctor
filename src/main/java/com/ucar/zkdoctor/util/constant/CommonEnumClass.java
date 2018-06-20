package com.ucar.zkdoctor.util.constant;

/**
 * Description: 通用的一些枚举类
 * Created on 2018/1/9 11:45
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CommonEnumClass {

    /**
     * 报警形式枚举类
     */
    public enum AlertForm {
        /**
         * 邮件
         */
        MAIL(0, "邮件"),

        /**
         * 短信
         */
        MESSAGE(1, "短信"),

        /**
         * 邮件+短信
         */
        MAILADNMESSAGE(2, "邮件+短信");

        private int alert;
        private String desc;

        AlertForm(int alert, String desc) {
            this.alert = alert;
            this.desc = desc;
        }

        public int getAlert() {
            return alert;
        }

        public String getDesc() {
            return desc;
        }

        public static String getDescByAlert(int alert) {
            for (AlertForm status : AlertForm.values()) {
                if (status.getAlert() == alert) {
                    return status.getDesc();
                }
            }
            return "";
        }
    }

}
