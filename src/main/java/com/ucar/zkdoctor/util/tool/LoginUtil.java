package com.ucar.zkdoctor.util.tool;

import org.apache.commons.lang.StringUtils;

/**
 * Description: ldap 登录工具
 * Created on 2017/12/18 10:24
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class LoginUtil {

    /**
     * LDAP登录校验
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static boolean ldapLogin(String username, String password) {
        // TODO 自行增加ldap校验过程
        return true;
    }

    /**
     * 用户密码校验
     *
     * @param loginPassword 登录密码
     * @param userPassword  用户密码
     * @return
     */
    public static boolean registerLogin(String loginPassword, String userPassword) {
        String shaPassword = SHACryptor.encode(loginPassword);
        return StringUtils.isNotBlank(shaPassword) && shaPassword.equals(userPassword);
    }
}
