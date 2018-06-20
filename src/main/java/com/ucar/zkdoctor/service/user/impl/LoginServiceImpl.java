package com.ucar.zkdoctor.service.user.impl;

import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.service.user.LoginService;
import com.ucar.zkdoctor.service.user.UserService;
import com.ucar.zkdoctor.util.config.ConfigUtil;
import com.ucar.zkdoctor.util.constant.UserEnumClass.LoginCheckType;
import com.ucar.zkdoctor.util.tool.LoginUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Description: 登录服务
 * Created on 2017/12/15 11:05
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserService userService;

    @Override
    public boolean login(String userName, String password) {
        LoginCheckType loginCheckType = ConfigUtil.getLoginCheckType();
        switch (loginCheckType) {
            case REGISTER:
                User user = userService.getUserByName(userName);
                if (user != null) { // 进行用户名密码校验
                    return LoginUtil.registerLogin(password, user.getPassword());
                }
                break;
            case LDAP:
                boolean login = LoginUtil.ldapLogin(userName, password);
                if (login) { // 如果使用ldap登录，用户之前未登录，则保存用户信息
                    User loginUser = userService.getUserByName(userName);
                    if (loginUser == null) {
                        userService.saveNewUser(userName);
                    }
                }
                return login;
        }
        return false;
    }
}
