package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.InstanceState;

import java.util.List;

/**
 * Description: 实例状态Dao
 * Created on 2018/1/9 11:04
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface InstanceStateDao {

    /**
     * 插入新的实例状态信息
     *
     * @param instanceState 实例状态
     * @return
     */
    boolean insertInstanceState(InstanceState instanceState);

    /**
     * 通过实例id获取最新实例状态信息
     *
     * @param instanceId 实例id
     * @return
     */
    InstanceState getInstanceStateByInstanceId(int instanceId);

    /**
     * 获取某集群下所有实例最近状态信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<InstanceState> getInstanceStateByClusterId(int clusterId);

    /**
     * 删除某实例状态信息
     *
     * @param instanceId 实例id
     * @return
     */
    boolean deleteInstanceStateByInstanceId(int instanceId);
}
