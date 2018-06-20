package com.ucar.zkdoctor.service.user.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.bo.UserSearchBO;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.service.user.UserService;
import com.ucar.zkdoctor.util.constant.UserEnumClass.UserRoleEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Description: 用户服务测试
 * Created on 2018/1/5 15:41
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class UserServiceImplTest extends BaseTest {

    @Resource
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        User user = userService.getUserById(1);
        if (user != null) {
            System.out.println(user.toString());
        } else {
            System.out.println("User is null.");
        }
    }

    @Test
    public void testGetUserByName() throws Exception {
        User user = userService.getUserByName("admin3");
        if (user != null) {
            System.out.println(user.toString());
        } else {
            System.out.println("User is null.");
        }
    }

    @Test
    public void testSaveNewUserOnlyName() throws Exception {
        boolean save = userService.saveNewUser("admin");
        System.out.println("result is : " + save);

    }

    @Test
    public void testSaveNewUserByParams() throws Exception {
        boolean save = userService.saveNewUser("admin3", "管理员", "admin3@test.com",
                "15111111111", UserRoleEnum.ADMIN.getUserRole());
        System.out.println("result is : " + save);
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = userService.getUserByName("admin");
        if (user != null) {
            System.out.println(user.toString());
            user.setMobile("22222222222");
            user.setUserRole(UserRoleEnum.GENERAL.getUserRole());
            user.setEmail("admin@12.com");
            user.setChName("管理员");
            user.setUserName("test");
            user.setModifyTime(new Date());
            userService.updateUser(user);
            user = userService.getUserByName("admin");
            System.out.println(user.toString());
        }
    }

    @Test
    public void testDeleteUserById() throws Exception {
        System.out.println(userService.deleteUserById(2));
    }

    @Test
    public void testDeleteUserByName() throws Exception {
        System.out.println(userService.deleteUserByName("admin"));
    }

    @Test
    public void testGetAllUsersByParams() throws Exception {
        UserSearchBO userSearchBO = new UserSearchBO();
        userSearchBO.setUserRole(UserRoleEnum.ADMIN.getUserRole());
        List<User> alluser = userService.getAllUsersByParams(userSearchBO);
        if (CollectionUtils.isNotEmpty(alluser)) {
            for (User user : alluser) {
                System.out.println(user.toString());
            }
        }
    }
}