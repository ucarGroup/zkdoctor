package com.ucar.zkdoctor.web.controller.manager;

import com.ucar.zkdoctor.pojo.po.ServiceLine;
import com.ucar.zkdoctor.pojo.po.SysConfig;
import com.ucar.zkdoctor.pojo.vo.PageVO;
import com.ucar.zkdoctor.service.system.ConfigService;
import com.ucar.zkdoctor.service.system.ServiceLineService;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 系统配置信息Controller
 * Created on 2018/3/29 17:54
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/manage/system")
public class SystemConfigManageController extends BaseController {

    @Resource
    private ConfigService configService;

    @Resource
    private ServiceLineService serviceLineService;

    /**
     * 查询系统配置信息
     *
     * @return
     */
    @RequestMapping(value = "/config/query")
    @ResponseBody
    public ConResult doConfigQuery() {
        try {
            List<SysConfig> sysConfigList = configService.getAllSysConfigIncludeDefault();
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("data", sysConfigList);
            PageVO pageVO = new PageVO();
            pageVO.setTotal(sysConfigList.size());
            result.put("page", pageVO);
            return ConResult.success(result);
        } catch (RuntimeException e) {
            return ConResult.fail("获取系统配置信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新系统配置信息
     *
     * @param sysConfig 待更新系统配置项
     * @return
     */
    @RequestMapping(value = "/config/update", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doConfigUpdate(SysConfig sysConfig) {
        try {
            boolean addResult = configService.insertSysConfig(sysConfig);
            if (addResult) {
                return new ConResult(true, "更新配置成功", null);
            } else {
                return ConResult.fail("更新配置失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("更新配置失败：" + e.getMessage());
        }
    }

    /**
     * 删除系统配置信息
     *
     * @param configName 待删除配置项名称
     * @return
     */
    @RequestMapping(value = "/config/delete", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doConfigDelete(String configName) {
        try {
            boolean deleteResult = configService.deleteSystemByName(configName);
            if (deleteResult) {
                return new ConResult(true, "删除配置项成功，将走系统默认配置值", null);
            } else {
                return ConResult.fail("删除配置项失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("删除配置项失败：" + e.getMessage());
        }
    }

    /**
     * 插入新业务线信息
     *
     * @param serviceLine 新业务线信息
     * @return
     */
    @RequestMapping(value = "/serviceLine/insert", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doServiceLineInsert(ServiceLine serviceLine) {
        try {
            ServiceLine oldServiceLine = serviceLineService.getServiceLineByName(serviceLine.getServiceLineName());
            if (oldServiceLine != null) {
                return ConResult.fail("该业务线已经存在，请检查。");
            }
            boolean insertResult = serviceLineService.insertServiceLine(serviceLine);
            if (insertResult) {
                return new ConResult(true, "新增业务线配置成功", null);
            } else {
                return ConResult.fail("新增业务线配置失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("新增业务线配置失败：" + e.getMessage());
        }
    }

    /**
     * 更新业务线信息
     *
     * @param serviceLine 业务线信息
     * @return
     */
    @RequestMapping(value = "/serviceLine/update", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doServiceLineUpdate(ServiceLine serviceLine) {
        try {
            boolean updateResult = serviceLineService.updateServiceLine(serviceLine);
            if (updateResult) {
                return new ConResult(true, "更新业务线配置成功", null);
            } else {
                return ConResult.fail("更新业务线配置失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("更新业务线配置失败：" + e.getMessage());
        }
    }

    /**
     * 删除业务线信息
     *
     * @param serviceLineName 业务线名称
     * @return
     */
    @RequestMapping(value = "/serviceLine/delete", method = RequestMethod.POST)
    @ResponseBody
    public ConResult doServiceLineDelete(String serviceLineName) {
        try {
            boolean deleteResult = serviceLineService.deleteServiceLineByName(serviceLineName);
            if (deleteResult) {
                return new ConResult(true, "删除业务线" + serviceLineName + "成功", null);
            } else {
                return ConResult.fail("删除业务线" + serviceLineName + "失败，请查看相关日志。");
            }
        } catch (RuntimeException e) {
            return ConResult.fail("删除业务线" + serviceLineName + "失败：" + e.getMessage());
        }
    }
}
