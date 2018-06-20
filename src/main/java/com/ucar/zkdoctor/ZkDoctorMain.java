package com.ucar.zkdoctor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Description: 启动类
 * Created on 2017/12/18 17:09
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@SpringBootApplication
@MapperScan({"com.ucar.zkdoctor.dao.mysql", "com.ucar.zkdoctor.business.clusterMonitorAlarmTask.dao"})
public class ZkDoctorMain extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ZkDoctorMain.class, args);
    }

    /**
     * 使用外部tomcat容器时，需继承SpringBootServletInitializer，重写configure方法
     *
     * @param builder SpringApplicationBuilder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.sources(this.getClass());
        return super.configure(builder);
    }
}
