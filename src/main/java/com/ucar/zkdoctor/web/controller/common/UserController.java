package com.ucar.zkdoctor.web.controller.common;

import com.ucar.zkdoctor.pojo.bo.UserSearchBO;
import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.pojo.vo.PageVO;
import com.ucar.zkdoctor.service.user.UserService;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 用户管理控制器
 * Created on 2017/12/26 15:31
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 查询用户信息
     *
     * @param userSearchBO 用户查询条件
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public ConResult doQuery(UserSearchBO userSearchBO) {
        List<User> userList = userService.getAllUsersByParams(userSearchBO);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", userList);
        PageVO pageVO = new PageVO();
        if (userList != null) {
            pageVO.setTotal(userList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    /**
     * 删除用户信息
     *
     * @param userId 需删除的用户id
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doDelete(Integer userId) {
        if (userId != null) {
            boolean delete = userService.deleteUserById(userId);
            LOGGER.warn("Delete user id:{}, result is {}.", userId, delete);
            if (delete) {
                return new ConResult(true, "删除用户成功！", null);
            } else {
                return ConResult.fail();
            }
        }
        LOGGER.warn("Delete user failed because userId is null.");
        return ConResult.fail("删除用户失败，请查看相关日志信息。");
    }

    /**
     * 创建新用户信息
     *
     * @param user 新用户信息
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doCreate(User user) {
        boolean create = userService.saveNewUser(user);
        LOGGER.warn("Create user {}, result is {}.", user == null ? "null" : user.toString(), create);
        if (create) {
            return new ConResult(true, "创建用户成功！", null);
        } else {
            return ConResult.fail("创建用户失败，请查看相关日志信息。");
        }
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doUpdate(User user) {
        if (user.getId() == null) {
            return ConResult.fail("更新用户失败，用户id为NULL。");
        }
        boolean update = userService.updateUser(user);
        LOGGER.info("Update user {}, result is {}.", user == null ? "null" : user.getUserName(), update);
        if (update) {
            return new ConResult(true, "更新用户成功！", null);
        } else {
            return ConResult.fail("更新用户失败，请查看相关日志信息。");
        }
    }

    /**
     * 获取所有用户信息
     *
     * @return
     */
    @RequestMapping("/listAll")
    @ResponseBody
    public ConResult getAllUsers() {
        try {
            List<User> list = userService.getAllUsersByParams(null);
            return ConResult.success(list);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ConResult.fail("获取所有用户信息失败，请查看相关日志信息。");
        }
    }

}
