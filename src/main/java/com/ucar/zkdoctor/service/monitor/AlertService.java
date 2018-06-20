package com.ucar.zkdoctor.service.monitor;

/**
 * Description: 报警服务接口
 * Created on 2018/2/5 16:15
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface AlertService {

    /**
     * 集群与实例相关报警
     *
     * @param clusterId        集群id
     * @param instanceId       实例id（null表示该报警为集群级报警）
     * @param currentValue     当前值
     * @param alertValue       报警阈值
     * @param alertForm        报警形式
     * @param alertInfo        报警信息
     * @param alertUnit        报警值单位
     * @param time             发生报警时间
     * @param isValueThreshold 此次报警是否为超过阈值类型的报警
     */
    void alert(int clusterId, Integer instanceId, String currentValue, String alertValue, int alertForm,
               String alertInfo, String alertUnit, String time, Boolean isValueThreshold);

    /**
     * 机器相关报警
     *
     * @param clusterId    集群id
     * @param machineId    机器id
     * @param currentValue 当前值
     * @param alertValue   报警阈值
     * @param alertForm    报警形式
     * @param alertInfo    报警信息
     * @param alertUnit    报警值单位
     * @param time         发生报警时间
     */
    void alert(int clusterId, Integer machineId, String currentValue, String alertValue, int alertForm,
               String alertInfo, String alertUnit, String time);

}
