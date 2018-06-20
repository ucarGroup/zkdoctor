package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 用户查询信息
 * Created on 2018/1/8 15:21
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class UserSearchBO implements Serializable {

    private static final long serialVersionUID = 4767886499383494098L;

    /**
     * 用户名(英文，唯一识别用户。默认邮箱前缀)
     */
    private String userName;

    /**
     * 中文名
     */
    private String chName;

    /**
     * 用户域账户邮箱
     */
    private String email;

    /**
     * 用户手机
     */
    private String mobile;

    /**
     * 用户类型（UserEnumClass.UserRoleEnum）
     */
    private Integer userRole;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }
}
