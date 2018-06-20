package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.bo.MonitorIndicatorSearchBO;
import com.ucar.zkdoctor.pojo.po.MonitorIndicator;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 监控指标Dao
 * Created on 2018/2/6 11:30
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface MonitorIndicatorDao {

    /**
     * 保存监控指标
     *
     * @param monitorIndicator 监控指标
     * @return
     */
    boolean insertIndicator(MonitorIndicator monitorIndicator);

    /**
     * 更新监控指标
     *
     * @param monitorIndicator 监控指标
     * @return
     */
    boolean updateIndicator(MonitorIndicator monitorIndicator);

    /**
     * 修改监控指标开关
     *
     * @param id       监控指标id
     * @param switchOn 开关状态
     * @return
     */
    boolean updateIndicatorSwitchOn(@Param("id") int id, @Param("switchOn") boolean switchOn);

    /**
     * 根据监控指标id查询监控指标信息
     *
     * @param id 监控指标id
     * @return
     */
    MonitorIndicator getIndicatorByIndicatorId(int id);

    /**
     * 根据监控指标类名查询监控指标信息
     *
     * @param className 监控类名
     * @return
     */
    MonitorIndicator getIndicatorByClassName(String className);

    /**
     * 根据查询条件查询监控指标列表
     *
     * @param monitorIndicatorSearchBO 监控指标查询条件
     * @return
     */
    List<MonitorIndicator> getIndicatorsByParams(MonitorIndicatorSearchBO monitorIndicatorSearchBO);

    /**
     * 删除某一监控指标
     *
     * @param id 监控指标id
     * @return
     */
    boolean deleteIndicatorByIndicatorId(int id);
}
