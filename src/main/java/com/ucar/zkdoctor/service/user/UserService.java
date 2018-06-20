package com.ucar.zkdoctor.service.user;

import com.ucar.zkdoctor.pojo.bo.UserSearchBO;
import com.ucar.zkdoctor.pojo.po.User;

import java.util.List;

/**
 * Description: 用户服务接口
 * Created on 2018/1/5 15:06
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface UserService {

    /**
     * 根据id获取用户信息
     *
     * @param userId 用户id
     * @return
     */
    User getUserById(int userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param userName 用户名
     * @return
     */
    User getUserByName(String userName);

    /**
     * 插入新的用户信息
     *
     * @param userName 用户名称
     * @return
     */
    boolean saveNewUser(String userName);

    /**
     * 插入新的用户信息
     *
     * @param userName 用户名称
     * @param chName   用户中文名
     * @param email    邮箱
     * @param mobile   手机号
     * @param userRole 用户角色
     * @return
     */
    boolean saveNewUser(String userName, String chName, String email, String mobile, int userRole);

    /**
     * 插入新的用户信息
     *
     * @param user 新用户信息
     * @return
     */
    boolean saveNewUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户新信息
     * @return
     */
    boolean updateUser(User user);

    /**
     * 通过用户id删除用户
     *
     * @param userId 用户id
     * @return
     */
    boolean deleteUserById(int userId);

    /**
     * 通过用户名删除用户
     *
     * @param userName 用户名
     * @return
     */
    boolean deleteUserByName(String userName);

    /**
     * 获取所有用户信息
     *
     * @return
     */
    List<User> getAllUsersByParams(UserSearchBO userSearchBO);
}
