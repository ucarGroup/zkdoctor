package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.po.User;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Description: 用户Dao测试
 * Created on 2018/1/5 11:01
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class UserDaoTest extends BaseTest {

    @Resource
    private UserDao userDao;

    @Test
    public void testInsertNewUser() throws Exception {
        User newUser = new User();
        newUser.setUserName("admin");
        newUser.setChName("管理员");
        newUser.setEmail("admin@ucarinc.com");
        newUser.setUserRole(2);
        boolean result = userDao.insertNewUser(newUser);
        System.out.println("result is : " + result + ",user id is : " + newUser.getId());
    }

}