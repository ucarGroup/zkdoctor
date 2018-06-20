package com.ucar.zkdoctor.pojo.po;

import com.ucar.zkdoctor.util.tool.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 定时任务触发器信息。对应表：zk_qrtz_triggers
 * Created on 2018/1/16 16:53
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class TriggerInfo implements Serializable {

    private static final long serialVersionUID = 7052332401092298453L;

    /**
     * 调度器名称
     */
    private String schedName;

    /**
     * 触发器名称
     */
    private String triggerName;

    /**
     * 触发器组名称
     */
    private String triggerGroup;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名称
     */
    private String jobGroup;

    /**
     * 描述
     */
    private String description;

    /**
     * 下次执行时间
     */
    private long nextFireTime;

    /**
     * 下次执行时间字符串表示，yyyy-mm-dd hh:mm:ss
     */
    private String nextFireTimeStr;

    /**
     * 上一次执行时间
     */
    private long prevFireTime;

    /**
     * 上一次执行时间字符串表示，yyyy-mm-dd hh:mm:ss
     */
    private String prevFireTimeStr;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 触发器状态
     */
    private String triggerState;

    /**
     * 触发器类型
     */
    private String triggerType;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 开始执行时间字符串表示，yyyy-mm-dd hh:mm:ss
     */
    private String startTimeStr;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * calendar名称
     */
    private String calendarName;

    /**
     * misfire
     */
    private short misfireInstr;

    /**
     * cron表达式
     */
    private String cron;

    @Override
    public String toString() {
        return "TriggerInfo{" +
                "schedName='" + schedName + '\'' +
                ", triggerName='" + triggerName + '\'' +
                ", triggerGroup='" + triggerGroup + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", description='" + description + '\'' +
                ", nextFireTime=" + nextFireTime +
                ", nextFireTimeStr='" + getNextFireTimeStr() + '\'' +
                ", prevFireTime=" + prevFireTime +
                ", prevFireTimeStr='" + getPrevFireTimeStr() + '\'' +
                ", priority=" + priority +
                ", triggerState='" + triggerState + '\'' +
                ", triggerType='" + triggerType + '\'' +
                ", startTime=" + startTime +
                ", startTimeStr='" + getStartTimeStr() + '\'' +
                ", endTime=" + endTime +
                ", calendarName='" + calendarName + '\'' +
                ", misfireInstr=" + misfireInstr +
                ", cron='" + cron + '\'' +
                '}';
    }

    public String getSchedName() {
        return schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public String getNextFireTimeStr() {
        return DateUtil.formatYYYYMMddHHMMss(new Date(nextFireTime));
    }

    public void setNextFireTimeStr(String nextFireTimeStr) {
        this.nextFireTimeStr = nextFireTimeStr;
    }

    public long getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(long prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public String getPrevFireTimeStr() {
        return DateUtil.formatYYYYMMddHHMMss(new Date(prevFireTime));
    }

    public void setPrevFireTimeStr(String prevFireTimeStr) {
        this.prevFireTimeStr = prevFireTimeStr;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getStartTimeStr() {
        return DateUtil.formatYYYYMMddHHMMss(new Date(startTime));
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public short getMisfireInstr() {
        return misfireInstr;
    }

    public void setMisfireInstr(short misfireInstr) {
        this.misfireInstr = misfireInstr;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
