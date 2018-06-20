package com.ucar.zkdoctor.service.user;

/**
 * Description: 登录服务
 * Created on 2017/12/15 11:06
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface LoginService {

    /**
     * 用户登录校验
     *
     * @param userName 用户名
     * @param password 用户密码
     * @return
     */
    boolean login(String userName, String password);
}
