package com.ucar.zkdoctor.service.user.impl;

import com.ucar.zkdoctor.dao.mysql.UserDao;
import com.ucar.zkdoctor.pojo.bo.UserSearchBO;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.service.cluster.ClusterService;
import com.ucar.zkdoctor.service.user.UserService;
import com.ucar.zkdoctor.util.constant.UserEnumClass.UserRoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: 用户服务
 * Created on 2018/1/5 15:06
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;

    @Resource
    private ClusterService clusterService;

    @Override
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByName(String userName) {
        return userDao.getUserByName(userName);
    }

    @Override
    public boolean saveNewUser(String userName) {
        return saveNewUser(userName, userName, null, null, UserRoleEnum.GENERAL.getUserRole());
    }

    @Override
    public boolean saveNewUser(String userName, String chName, String email, String mobile, int userRole) {
        User user = generateNewUser(userName, chName, email, mobile, userRole);
        if (user == null) {
            return false;
        }
        return saveNewUser(user);
    }

    @Override
    public boolean saveNewUser(User user) {
        if (user == null) {
            return false;
        }
        try {
            user.setCreateTime(new Date());
            return userDao.insertNewUser(user);
        } catch (Exception e) {
            LOGGER.warn("Save new user {} failed.", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null) {
            return false;
        }
        user.setModifyTime(new Date());
        return userDao.updateUser(user);
    }

    @Override
    public boolean deleteUserById(int userId) {
        // 删除用户时，同时删除该用户所有配置的报警信息，不影响最终删除结果
        clusterService.deleteAllAlarmUser(userId);
        return userDao.deleteUserById(userId);
    }

    @Override
    public boolean deleteUserByName(String userName) {
        return userDao.deleteUserByName(userName);
    }

    @Override
    public List<User> getAllUsersByParams(UserSearchBO userSearchBO) {
        return userDao.getAllUsersByParams(userSearchBO);
    }

    /**
     * 生成新用户信息
     *
     * @param userName 用户名称
     * @param chName   用户中文名
     * @param email    邮箱
     * @param mobile   手机号
     * @param userRole 用户角色
     * @return
     */
    private User generateNewUser(String userName, String chName, String email, String mobile, int userRole) {
        User user = getUserByName(userName);
        if (user != null) {
            LOGGER.warn("Save new user {} failed, because the userName exists.", userName);
            return null;
        }
        user = new User();
        user.setUserName(userName);
        user.setChName(chName);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserRole(userRole);
        user.setCreateTime(new Date());
        return user;
    }
}
