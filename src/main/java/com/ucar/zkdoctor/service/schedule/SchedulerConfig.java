package com.ucar.zkdoctor.service.schedule;

import com.ucar.zkdoctor.service.schedule.jobs.CleanCollectDataJob;
import com.ucar.zkdoctor.service.schedule.jobs.InstanceConnectionCollectJob;
import com.ucar.zkdoctor.service.schedule.jobs.MachineJob;
import com.ucar.zkdoctor.service.schedule.jobs.MonitorJob;
import com.ucar.zkdoctor.service.schedule.jobs.ZKJob;
import com.ucar.zkdoctor.util.constant.SchedulerConstant;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Description: quartz定时任务配置：job以及trigger
 * Created on 2018/1/29 11:02
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Configuration
@ConditionalOnProperty(name = "quartz.enabled", havingValue = "true")
public class SchedulerConfig {

    /**
     * 初始化设置JobFactory
     *
     * @param applicationContext 应用上下文
     * @return
     */
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    /**
     * 初始化quartz配置
     *
     * @return
     * @throws IOException
     */
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * 初始化定时任务调度器
     *
     * @param dataSource 数据源信息
     * @param jobFactory JobFactory
     * @param triggers   触发器列表
     * @param jobDetails job详细信息
     * @return
     * @throws Exception
     */
    @Bean
    public Scheduler schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory,
                                          Trigger[] triggers, JobDetail[] jobDetails) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
        factory.afterPropertiesSet();

        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        // 注册所有job信息
        if (jobDetails != null) {
            for (JobDetail jobDetail : jobDetails) {
                JobKey jobKey = jobDetail.getKey();
                if (!scheduler.checkExists(jobKey)) {
                    scheduler.addJob(jobDetail, false);
                }
            }
        }
        // 注册所有触发器信息
        if (triggers != null) {
            for (Trigger trigger : triggers) {
                TriggerKey triggerKey = trigger.getTriggerBuilder().build().getKey();
                if (scheduler.checkExists(triggerKey)) {
                    scheduler.unscheduleJob(triggerKey);
                    scheduler.scheduleJob(trigger);
                } else {
                    JobDetail jobDetail = (JobDetail) trigger.getJobDataMap().get("jobDetail");
                    JobKey jobKey = jobDetail.getKey();
                    if (scheduler.checkExists(jobKey)) {
                        scheduler.scheduleJob(trigger);
                    } else {
                        scheduler.scheduleJob(jobDetail, trigger);
                    }
                }
            }
        }
        scheduler.start();
        return scheduler;
    }

    /**
     * 集群信息收集Job
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean zkJobDetail() {
        return createJobDetail(ZKJob.class,
                SchedulerConstant.ZK_COLLECT_JOB_NAME, SchedulerConstant.ZK_COLLECT_JOB_GROUP);
    }

    /**
     * 历史数据清除job信息
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean cleanCollectDataJobDetail() {
        return createJobDetail(CleanCollectDataJob.class,
                SchedulerConstant.ZK_CLEAN_JOB_NAME, SchedulerConstant.ZK_CLEAN_JOB_GROUP);
    }

    /**
     * 历史数据清除定时任务触发器信息
     *
     * @param jobDetail      job信息
     * @param cronExpression cron表达式
     * @return
     */
    @Bean(name = "cleanCollectDataJobTrigger")
    public CronTriggerFactoryBean cleanCollectDataJobTrigger(@Qualifier("cleanCollectDataJobDetail") JobDetail jobDetail,
                                                             @Value(SchedulerConstant.ZK_CLEAN_JOB_CRON) String cronExpression) {
        return createCronTrigger(jobDetail, cronExpression,
                SchedulerConstant.ZK_CLEAN_TRIGGER_NAME, SchedulerConstant.ZK_CLEAN_TRIGGER_GROUP);
    }

    /**
     * 监控报警job信息
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean monitorJobDetail() {
        return createJobDetail(MonitorJob.class,
                SchedulerConstant.ZK_MONITOR_JOB_NAME, SchedulerConstant.ZK_MONITOR_JOB_GROUP);
    }

    /**
     * 监控报警定时任务触发器信息
     *
     * @param jobDetail      job信息
     * @param cronExpression cron表达式
     * @return
     */
    @Bean(name = "monitorJobTrigger")
    public CronTriggerFactoryBean monitorJobTrigger(@Qualifier("monitorJobDetail") JobDetail jobDetail,
                                                    @Value(SchedulerConstant.ZK_MONITOR_JOB_CRON) String cronExpression) {
        return createCronTrigger(jobDetail, cronExpression,
                SchedulerConstant.ZK_MONITOR_TRIGGER_NAME, SchedulerConstant.ZK_MONITOR_TRIGGER_GROUP);
    }

    /**
     * 机器信息收集job信息
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean machineJobDetail() {
        return createJobDetail(MachineJob.class,
                SchedulerConstant.MACHINE_COLLECT_JOB_NAME, SchedulerConstant.MACHINE_COLLECT_JOB_GROUP);
    }

    /**
     * 监控报警定时任务触发器信息
     *
     * @param jobDetail      job信息
     * @param cronExpression cron表达式
     * @return
     */
    @Bean(name = "machineJobTrigger")
    public CronTriggerFactoryBean machineJobTrigger(@Qualifier("machineJobDetail") JobDetail jobDetail,
                                                    @Value(SchedulerConstant.MACHINE_COLLECT_JOB_CRON) String cronExpression) {
        return createCronTrigger(jobDetail, cronExpression,
                SchedulerConstant.MACHINE_COLLECT_TRIGGER_NAME, SchedulerConstant.MACHINE_COLLECT_TRIGGER_GROUP);
    }

    /**
     * 实例连接信息收集job信息
     *
     * @return
     */
    @Bean
    public JobDetailFactoryBean instanceConnCollectJobDetail() {
        return createJobDetail(InstanceConnectionCollectJob.class,
                SchedulerConstant.INSTANCE_CONN_COLLECT_JOB_NAME, SchedulerConstant.INSTANCE_CONN_COLLECT_JOB_GROUP);
    }

    /**
     * 实例连接信息收集定时任务触发器信息
     *
     * @param jobDetail      job信息
     * @param cronExpression cron表达式
     * @return
     */
    @Bean(name = "instanceConnectionCollectJobTrigger")
    public CronTriggerFactoryBean instanceConnectionCollectJobTrigger(@Qualifier("instanceConnCollectJobDetail") JobDetail jobDetail,
                                                                      @Value(SchedulerConstant.INSTANCE_CONN_COLLECT_JOB_CRON) String cronExpression) {
        return createCronTrigger(jobDetail, cronExpression,
                SchedulerConstant.INSTANCE_CONN_COLLECT_TRIGGER_NAME, SchedulerConstant.INSTANCE_CONN_COLLECT_TRIGGER_GROUP);
    }

    /**
     * 创建jboDetail信息
     *
     * @param jobClass job类
     * @return
     */
    private JobDetailFactoryBean createJobDetail(Class jobClass, String keyName, String group) {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);
        factoryBean.setName(keyName);
        factoryBean.setGroup(group);
        return factoryBean;
    }

    /**
     * 创建cron触发器
     *
     * @param jobDetail      job信息
     * @param cronExpression cron表达式
     * @param triggerName    触发器名称
     * @param triggerGroup   触发器组
     * @return
     */
    private CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression,
                                                     String triggerName, String triggerGroup) {
        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setName(triggerName);
        factoryBean.setGroup(triggerGroup);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
        return factoryBean;
    }

}

