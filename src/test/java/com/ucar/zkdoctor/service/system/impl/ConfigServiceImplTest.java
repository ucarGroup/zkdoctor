package com.ucar.zkdoctor.service.system.impl;

import com.ucar.zkdoctor.BaseTest;
import com.ucar.zkdoctor.pojo.po.SysConfig;
import com.ucar.zkdoctor.service.system.ConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 系统配置测试类
 * Created on 2018/1/23 14:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ConfigServiceImplTest extends BaseTest {

    @Resource
    private ConfigService configService;

    @Test
    public void insertSysConfig() throws Exception {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigName("SSH_USERNAME");
        sysConfig.setConfigValue("zkdoctor");
        sysConfig.setConfigDesc("ssh登录用户名");
        boolean result = configService.insertSysConfig(sysConfig);
        System.out.println("result is " + result);
    }

    @Test
    public void getSysConfigByName() throws Exception {
        SysConfig sysConfig = configService.getSysConfigByName("SSH_USERNAME");
        if (sysConfig != null) {
            System.out.println(sysConfig);
        } else {
            System.out.println("sysConfig is NULL.");
        }
    }

    @Test
    public void getAllSysConfig() throws Exception {
        List<SysConfig> sysConfigList = configService.getAllSysConfig();
        if (CollectionUtils.isNotEmpty(sysConfigList)) {
            for (SysConfig sysConfig : sysConfigList) {
                System.out.println(sysConfig);
            }
        } else {
            System.out.println("all sysConfig is NULL.");
        }
    }

    @Test
    public void deleteSystemByName() throws Exception {
        boolean result = configService.deleteSystemByName("SSH_USERNAME");
        System.out.println("result is " + result);
    }

    @Test
    public void testGetAllSysConfigIncludeDefault() throws Exception {
        List<SysConfig> sysConfigList = configService.getAllSysConfigIncludeDefault();
        if (CollectionUtils.isNotEmpty(sysConfigList)) {
            for (SysConfig sysConfig : sysConfigList) {
                System.out.println(sysConfig);
            }
        } else {
            System.out.println("all sysConfig is NULL.");
        }
    }
}