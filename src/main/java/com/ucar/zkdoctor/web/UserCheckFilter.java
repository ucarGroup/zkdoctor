package com.ucar.zkdoctor.web;

import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.util.constant.UserEnumClass;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Description: 用户权限校验，包括session是否过期校验
 * Created on 2018/3/16 14:39
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*", filterName = "userCheckFilter")
public class UserCheckFilter extends BaseController implements Filter {

    /**
     * 管理员或超级管理员操作权限控制的uri信息
     */
    private static final String ADMIN_OPERATE_URI = "/manage/";

    /**
     * 超级管理员权限操作的uri信息
     */
    private static final String SUPER_OPERATE_URI = "/supermanage/";

    /**
     * 无需验证session的请求
     */
    private static final Set<String> FILTER_SETS = new HashSet<String>();

    static {
        FILTER_SETS.add("register");
        FILTER_SETS.add("checkUserExists");
        FILTER_SETS.add("login");
        FILTER_SETS.add("logout");
        FILTER_SETS.add("index.html");
        FILTER_SETS.add(".js");
        FILTER_SETS.add(".css");
        FILTER_SETS.add(".ico");
        FILTER_SETS.add(".png");
        FILTER_SETS.add(".gif");
        FILTER_SETS.add("/user/info");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        // 过滤无需验证的请求
        for (String filterName : FILTER_SETS) {
            if (uri.contains(filterName)) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        // 过滤context path
        if (uri.equals(request.getContextPath() + "/")) {
            filterChain.doFilter(request, response);
            return;
        }
        User user = retrieveUser(request);
        if (user == null) { // session中无用户信息，认为session超时
            // 暂以正确的状态码，错误的提示信息，返回
            response.sendError(200, "Timeout");
            return;
        } else { // 用户操作权限校验
            if ((uri.contains(ADMIN_OPERATE_URI) &&
                    user.getUserRole() != UserEnumClass.UserRoleEnum.ADMIN.getUserRole() &&
                    user.getUserRole() != UserEnumClass.UserRoleEnum.SUPERADMIN.getUserRole()) ||
                    (uri.contains(SUPER_OPERATE_URI) &&
                            user.getUserRole() != UserEnumClass.UserRoleEnum.SUPERADMIN.getUserRole())) {
                // 暂以正确的状态码，错误的提示信息，返回
                response.sendError(200, "Permission denied");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
