package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.pojo.BaseTimeLineObject;

/**
 * Description: 用户信息
 * Created on 2017/12/18 18:47
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class User extends BaseTimeLineObject {

    private static final long serialVersionUID = 6315100437113390004L;

    /**
     * 用户id（自增id）
     */
    private Integer id;

    /**
     * 用户名(英文，唯一识别用户。默认邮箱前缀)
     */
    private String userName;

    /**
     * 保留。可用于无LDAP验证使用
     */
    private String password;

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

    /**
     * 预留参数
     */
    private String param1;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", chName='" + chName + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", userRole=" + userRole +
                ", param1='" + param1 + '\'' +
                "} " + super.toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
}
