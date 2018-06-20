package com.ucar.zkdoctor.service.system;

import com.ucar.zkdoctor.service.monitor.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Description: 初始化工作
 * Created on 2018/1/29 15:43
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Component
public class Init implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Init.class);

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        LOGGER.info("Init...");
        ConfigurableApplicationContext context = event.getApplicationContext();
        // 初始化监控报警信息
        try {
            MonitorService monitorService = context.getBean("monitorService", MonitorService.class);
            monitorService.initMonitor();
        } catch (Exception e) {
            LOGGER.error("Init all monitor info failed.", e);
        }
        // 初始化系统配置信息
        try {
            ConfigService configService = context.getBean("configService", ConfigService.class);
            configService.initConfig();
        } catch (Exception e) {
            LOGGER.error("Init all configuration info failed.", e);
        }
        // 初始化业务线默认配置信息
        try {
            ServiceLineService serviceLineService = context.getBean("serviceLineService", ServiceLineService.class);
            serviceLineService.initDefaultServiceLine();
        } catch (Exception e) {
            LOGGER.error("Init all service lines info failed.", e);
        }
    }
}