package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.bo.UserSearchBO;
import com.ucar.zkdoctor.pojo.po.User;

import java.util.List;

/**
 * Description: 用户信息Dao
 * Created on 2017/12/18 10:17
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface UserDao {

    /**
     * 根据id获取用户信息
     *
     * @param id 用户id
     * @return
     */
    User getUserById(int id);

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
     * @param user 用户信息
     * @return
     */
    boolean insertNewUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
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
