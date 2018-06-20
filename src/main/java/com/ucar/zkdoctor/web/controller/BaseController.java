package com.ucar.zkdoctor.web.controller;

import com.ucar.zkdoctor.pojo.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 控制器基类
 * Created on 2017/12/18 9:50
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class BaseController {

    /**
     * 记录用户session信息
     *
     * @param request
     * @param userBean
     */
    protected void bindUserToSession(HttpServletRequest request, User userBean) {
        request.getSession().setAttribute("user", userBean);
    }

    /**
     * 查询用户session信息
     *
     * @param request
     * @return
     */
    protected User retrieveUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    /**
     * 清空用户session信息
     *
     * @param request
     */
    protected void clearUserSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
