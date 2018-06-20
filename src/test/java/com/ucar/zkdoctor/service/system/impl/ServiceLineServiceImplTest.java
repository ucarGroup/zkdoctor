package com.ucar.zkdoctor.service.system.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.po.ServiceLine;
import com.ucar.zkdoctor.service.system.ServiceLineService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 业务线相关服务测试类
 * Created on 2018/4/10 13:51
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ServiceLineServiceImplTest extends BaseTest {

    @Resource
    private ServiceLineService serviceLineService;

    @Test
    public void insertServiceLine() throws Exception {
        ServiceLine serviceLine = new ServiceLine();
        serviceLine.setServiceLineName("ucar");
        serviceLine.setServiceLineDesc("专车");
        boolean result = serviceLineService.insertServiceLine(serviceLine);
        System.out.println("Insert result is " + result);
    }

    @Test
    public void getServiceLineByName() throws Exception {
        ServiceLine serviceLine = serviceLineService.getServiceLineByName("ucar");
        System.out.println(serviceLine);
    }

    @Test
    public void getAllServiceLine() throws Exception {
        List<ServiceLine> serviceLineList = serviceLineService.getAllServiceLine();
        if (CollectionUtils.isNotEmpty(serviceLineList)) {
            for (ServiceLine serviceLine : serviceLineList) {
                System.out.println(serviceLine);
            }
        } else {
            System.out.println("Result is empty");
        }
    }

    @Test
    public void updateServiceLine() throws Exception {
        ServiceLine serviceLine = serviceLineService.getServiceLineByName("ucar");
        if (serviceLine != null) {
            serviceLine.setServiceLineDesc("测试");
            boolean result = serviceLineService.updateServiceLine(serviceLine);
            System.out.println("Update result is " + result);
        }
    }

    @Test
    public void deleteServiceLineByName() throws Exception {
        boolean result = serviceLineService.deleteServiceLineByName("ucar");
        System.out.println("Result is " + result);
    }

}