package com.ucar.zkdoctor.util.constant;

/**
 * Description: 用户相关枚举类
 * Created on 2018/1/9 11:38
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class UserEnumClass {

    /**
     * 用户角色枚举类
     */
    public enum UserRoleEnum {
        /**
         * 普通用户，所有只读操作权限
         */
        GENERAL(0, "普通用户"),

        /**
         * 管理员，所有读写操作权限
         */
        ADMIN(1, "管理员"),

        /**
         * 超级管理员，所有读写操作权限、SSH机器权限、默认报警用户等
         */
        SUPERADMIN(2, "超级管理员");

        private int userRole;
        private String desc;

        UserRoleEnum(int userRole, String desc) {
            this.userRole = userRole;
            this.desc = desc;
        }

        public int getUserRole() {
            return userRole;
        }

        public String getDesc() {
            return desc;
        }

        /**
         * 根据用户角色，返回角色名称
         *
         * @param userRole 用户角色
         * @return
         */
        public static String getDescByUserRole(int userRole) {
            for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
                if (roleEnum.getUserRole() == userRole) {
                    return roleEnum.getDesc();
                }
            }
            return "null";
        }

        /**
         * 根据用户角色，返回用户角色枚举
         *
         * @param userRole 用户角色
         * @return
         */
        public static UserRoleEnum getUserRoleEnumByUserRole(int userRole) {
            for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
                if (roleEnum.getUserRole() == userRole) {
                    return roleEnum;
                }
            }
            return null;
        }
    }

    /**
     * 用户登录模式枚举
     */
    public enum LoginCheckType {
        /**
         * 注册用户登录模式
         */
        REGISTER(0, "register"),

        /**
         * 内部ldap登录模式
         */
        LDAP(1, "ldap");

        private int loginCheckType;
        private String desc;

        LoginCheckType(int loginCheckType, String desc) {
            this.loginCheckType = loginCheckType;
            this.desc = desc;
        }

        public int getLoginCheckType() {
            return loginCheckType;
        }

        public String getDesc() {
            return desc;
        }

        public static LoginCheckType getLoginCheckTypeEnumByType(String type) {
            for (LoginCheckType loginCheckType : LoginCheckType.values()) {
                if (loginCheckType.getDesc().equals(type)) {
                    return loginCheckType;
                }
            }
            // PS：如果没有，默认走注册模式
            return LoginCheckType.REGISTER;
        }
    }

}
