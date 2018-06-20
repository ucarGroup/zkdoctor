package com.ucar.zkdoctor;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Description: 支持外部数据源配置
 * Created on 2018/5/11 10:02
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Configuration
@ComponentScan
@PropertySource(value = {"classpath:/application.properties",
        "${spring.datasource.location}"}, ignoreResourceNotFound = true)
public class PersistenceJPAConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}