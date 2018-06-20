package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.bo.MonitorTaskSearchBO;
import com.ucar.zkdoctor.pojo.po.MonitorTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 监控任务Dao
 * Created on 2018/2/6 11:31
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MonitorTaskDao {

    /**
     * 创建监控任务
     *
     * @param monitorTask 监控任务
     * @return
     */
    boolean insertTask(MonitorTask monitorTask);

    /**
     * 更新某一监控任务
     *
     * @param monitorTask 监控任务
     * @return
     */
    boolean updateTask(MonitorTask monitorTask);

    /**
     * 修改监控任务开关
     *
     * @param id       监控任务id
     * @param switchOn 开关状态
     * @return
     */
    boolean updateTaskSwitchOn(@Param("id") int id, @Param("switchOn") boolean switchOn);

    /**
     * 获取某一监控任务
     *
     * @param id 监控任务id
     * @return
     */
    MonitorTask getTaskByTaskId(int id);

    /**
     * 根据查询条件，获取相关的监控任务列表
     *
     * @param monitorTaskSearchBO 监控任务查询条件
     * @return
     */
    List<MonitorTask> getTasksByParams(MonitorTaskSearchBO monitorTaskSearchBO);

    /**
     * 删除某一监控任务
     *
     * @param id 监控任务id
     * @return
     */
    boolean deleteTaskByTaskId(int id);

    /**
     * 删除某一监控指标下的所有监控任务
     *
     * @param indicatorId 监控指标id
     * @return
     */
    boolean deleteByIndicatorId(int indicatorId);

}
