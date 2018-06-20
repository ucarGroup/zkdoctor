package com.ucar.zkdoctor.service.view;

import com.ucar.zkdoctor.pojo.po.User;
import com.ucar.zkdoctor.pojo.vo.TreeNodeVO;

import java.util.List;

/**
 * Description: 菜单
 * Created on 2017/12/26 14:58
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MenuService {
    /**
     * 根据用户角色信息，返回显示的菜单
     *
     * @param user 用户信息
     * @return
     */
    List<TreeNodeVO> getTreeList(User user);
}
