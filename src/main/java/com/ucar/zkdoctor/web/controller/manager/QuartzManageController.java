package com.ucar.zkdoctor.web.controller.manager;

import com.ucar.zkdoctor.pojo.po.TriggerInfo;
import com.ucar.zkdoctor.pojo.vo.PageVO;
import com.ucar.zkdoctor.service.schedule.SchedulerService;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.apache.commons.lang.StringUtils;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 定时任务Controller
 * Created on 2018/2/5 18:08
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/manage/quartz")
public class QuartzManageController extends BaseController {

    @Resource
    private SchedulerService schedulerService;

    /**
     * 查询定时任务信息
     *
     * @param query 查询条件
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public ConResult doQuery(String query) {
        List<TriggerInfo> triggerInfoList;
        if (StringUtils.isBlank(query)) {
            triggerInfoList = schedulerService.getAllTriggers();
        } else {
            triggerInfoList = schedulerService.getTriggersByNameOrGroup(query);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", triggerInfoList == null ? new ArrayList<TriggerInfo>() : triggerInfoList);
        PageVO pageVO = new PageVO();
        if (triggerInfoList != null) {
            pageVO.setTotal(triggerInfoList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    /**
     * 删除定时任务信息
     *
     * @param triggerName  触发器名称
     * @param triggerGroup 触发器组名称
     * @return
     */
    @RequestMapping("/pauseTrigger")
    @ResponseBody
    public ConResult doPauseTrigger(String triggerName, String triggerGroup) {
        if (StringUtils.isBlank(triggerName) || StringUtils.isBlank(triggerGroup)) {
            return ConResult.fail("触发器名称与触发器组不能为NULL，请重试");
        }
        boolean pause = schedulerService.pauseTrigger(new TriggerKey(triggerName, triggerGroup));
        if (pause) {
            return new ConResult(true, "暂停定时任务成功！", null);
        } else {
            return ConResult.fail("暂停定时任务失败！");
        }
    }

    /**
     * 删除定时任务
     *
     * @param triggerName  触发器名称
     * @param triggerGroup 触发器组名称
     * @return
     */
    @RequestMapping("/removeTrigger")
    @ResponseBody
    public ConResult doRemoveTrigger(String triggerName, String triggerGroup) {
        if (StringUtils.isBlank(triggerName) || StringUtils.isBlank(triggerGroup)) {
            return ConResult.fail("触发器名称与触发器组不能为NULL，请重试");
        }
        boolean remove = schedulerService.removeTrigger(new TriggerKey(triggerName, triggerGroup));
        if (remove) {
            return new ConResult(true, "删除定时任务成功！", null);
        } else {
            return ConResult.fail("删除定时任务失败！");
        }
    }

    /**
     * 恢复定时任务
     *
     * @param triggerName  触发器名称
     * @param triggerGroup 触发器组名称
     * @return
     */
    @RequestMapping("/resumeTrigger")
    @ResponseBody
    public ConResult doResumeTrigger(String triggerName, String triggerGroup) {
        if (StringUtils.isBlank(triggerName) || StringUtils.isBlank(triggerGroup)) {
            return ConResult.fail("触发器名称与触发器组不能为NULL，请重试");
        }
        boolean resume = schedulerService.resumeTrigger(new TriggerKey(triggerName, triggerGroup));
        if (resume) {
            return new ConResult(true, "恢复定时任务成功！", null);
        } else {
            return ConResult.fail("恢复定时任务失败！");
        }
    }


}

