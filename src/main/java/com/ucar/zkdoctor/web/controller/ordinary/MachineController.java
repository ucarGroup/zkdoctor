package com.ucar.zkdoctor.web.controller.ordinary;

import com.ucar.zkdoctor.pojo.bo.MachineSearchBO;
import com.ucar.zkdoctor.pojo.po.InstanceInfo;
import com.ucar.zkdoctor.pojo.vo.MachineDetailVO;
import com.ucar.zkdoctor.pojo.vo.PageVO;
import com.ucar.zkdoctor.service.instance.InstanceService;
import com.ucar.zkdoctor.service.machine.MachineService;
import com.ucar.zkdoctor.web.ConResult;
import com.ucar.zkdoctor.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 机器Controller，普通用户权限相关操作
 * Created on 2018/1/19 16:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Controller
@RequestMapping("/machine")
public class MachineController extends BaseController {

    @Resource
    private MachineService machineService;

    @Resource
    private InstanceService instanceService;

    /**
     * 查询机器详情信息
     *
     * @param machineSearchBO 查询条件
     * @return
     */
    @RequestMapping("/query")
    @ResponseBody
    public ConResult doQuery(MachineSearchBO machineSearchBO) {
        List<MachineDetailVO> machineDetailVOList = machineService.getInstanceDetailVOByParams(machineSearchBO);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", machineDetailVOList == null ? new ArrayList<MachineDetailVO>() : machineDetailVOList);
        PageVO pageVO = new PageVO();
        if (machineDetailVOList != null) {
            pageVO.setTotal(machineDetailVOList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }

    /**
     * 查询机器上的实例信息
     *
     * @param machineId 机器id
     * @return
     */
    @RequestMapping("/queryMachineInstances")
    @ResponseBody
    public ConResult doQueryMachineInstances(Integer machineId) {
        if (machineId == null) {
            return ConResult.fail("机器id为NULL，请重试。");
        }
        List<InstanceInfo> instanceInfoList = instanceService.getByMachineId(machineId);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("data", instanceInfoList == null ? new ArrayList<InstanceInfo>() : instanceInfoList);
        PageVO pageVO = new PageVO();
        if (instanceInfoList != null) {
            pageVO.setTotal(instanceInfoList.size());
        }
        result.put("page", pageVO);
        return ConResult.success(result);
    }


}
