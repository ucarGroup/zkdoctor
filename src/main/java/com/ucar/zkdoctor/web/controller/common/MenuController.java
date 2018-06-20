package com.ucar.zkdoctor.web.controller.common;

import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.pojo.vo.TreeNodeVO;
import com.ucar.zkdoctor.service.view.MenuService;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Description: 菜单权限控制
 * Created on 2017/12/26 14:57
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private MenuService menuService;

    /**
     * 返回菜单栏
     *
     * @param request
     * @return
     */
    @RequestMapping("/treeList")
    @ResponseBody
    public ConResult treeList(HttpServletRequest request) {
        try {
            User user = retrieveUser(request);
            List<TreeNodeVO> treeList = menuService.getTreeList(user);
            return ConResult.success(treeList);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return ConResult.fail(e.getMessage());
        }
    }

}
