package com.ucar.zkdoctor.web.controller;

import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.service.user.LoginService;
import com.ucar.zkdoctor.service.user.UserService;
import com.ucar.zkdoctor.util.constant.UserEnumClass;
import com.ucar.zkdoctor.util.tool.SHACryptor;
import com.ucar.zkdoctor.web.ConResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 登录控制器
 * Created on 2017/12/18 9:53
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/user")
public class LoginController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private LoginService loginService;

    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param request
     * @param response
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doLogin(HttpServletRequest request, HttpServletResponse response,
                             String userName, String password) {
        try {
            LOGGER.info("User {} login.", userName);
            User loginUser;
            boolean loginResult = loginService.login(userName, password);
            if (loginResult) {
                loginUser = userService.getUserByName(userName);
            } else {
                LOGGER.warn("User {} login failed.", userName);
                return ConResult.fail("登录失败，请检查用户名或密码是否正确。");
            }
            bindUserToSession(request, loginUser);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", loginUser.getUserName());
            map.put("chName", loginUser.getChName());
            map.put("userRole", loginUser.getUserRole());
            map.put("userId", loginUser.getId());
            return ConResult.success(map);
        } catch (Exception e) {
            LOGGER.error("User {} login failed.", userName, e);
            return ConResult.fail("登录异常失败，请查看相关日志。");
        }
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ConResult logout(HttpServletRequest request) {
        try {
            clearUserSession(request);
            return ConResult.success();
        } catch (Exception e) {
            LOGGER.error("User logout failed.", e);
            return ConResult.fail("注销失败，请查看相关日志。");
        }
    }

    /**
     * 查看当前登录用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/info")
    @ResponseBody
    public ConResult info(HttpServletRequest request) {
        try {
            User user = retrieveUser(request);
            if (user == null) {
                return ConResult.fail("NO_USER");
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userName", user.getUserName());
            map.put("chName", user.getChName());
            map.put("userRole", user.getUserRole());
            map.put("userId", user.getId());
            return ConResult.success(map);
        } catch (Exception e) {
            LOGGER.error("Get user info failed.", e);
            return ConResult.fail("获取用户信息异常！");
        }
    }

    /**
     * 用户注册
     *
     * @param user 待注册用户信息
     * @return
     */
    @RequestMapping("/register")
    @ResponseBody
    public ConResult doRegister(User user) {
        try {
            if (user == null || StringUtils.isBlank(user.getUserName()) ||
                    StringUtils.isBlank(user.getPassword())) {
                return ConResult.fail("注册用户信息不能为空！请重新填写注册信息！");
            }
            User oldUser = userService.getUserByName(user.getUserName());
            if (oldUser != null) {
                return ConResult.fail("该用户名已经存在，请重新填写！");
            }
            // 默认注册用户为普通用户
            user.setUserRole(UserEnumClass.UserRoleEnum.GENERAL.getUserRole());
            // 密码加密存储
            user.setPassword(SHACryptor.encode(user.getPassword()));
            boolean register = userService.saveNewUser(user);
            if (register) {
                return new ConResult(true, "注册成功，请登录！", null);
            } else {
                return ConResult.fail("注册失败！请重新填写注册信息注册！");
            }
        } catch (Exception e) {
            LOGGER.error("Register new user info failed.", e);
            return ConResult.fail("注册异常！" + e.getMessage());
        }
    }

    /**
     * 校验用户名是否存在
     *
     * @param userName 用户名
     * @return
     */
    @RequestMapping("/checkUserExists")
    @ResponseBody
    public ConResult doCheckUserExists(String userName) {
        if (StringUtils.isBlank(userName)) {
            return ConResult.fail("用户名不能为空");
        }
        User oldUser = userService.getUserByName(userName);
        if (oldUser == null) {
            return ConResult.success(false);
        } else {
            return ConResult.success(true);
        }
    }
}
