package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.ClusterAlarmUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: 集群下配置的用户信息
 * Created on 2018/1/11 20:41
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ClusterAlarmUserDao {

    /**
     * 插入新的集群用户信息
     *
     * @param clusterAlarmUser 集群与用户对应信息
     * @return
     */
    boolean insertClusterAlarmUser(ClusterAlarmUser clusterAlarmUser);

    /**
     * 获取集群下的所有报警用户信息
     *
     * @param clusterId 集群id
     * @return
     */
    List<ClusterAlarmUser> getUserIdsByClusterId(int clusterId);

    /**
     * 获取用户对应的集群信息
     *
     * @param userId 用户id
     * @return
     */
    List<ClusterAlarmUser> getClusterIdsByUserId(int userId);

    /**
     * 删除集群与用户对应关系
     *
     * @param clusterId 集群id
     * @param userId    用户id
     * @return
     */
    boolean deleteAlarmUser(@Param("clusterId") int clusterId, @Param("userId") int userId);

    /**
     * 删除某个用户的所有报警设置
     *
     * @param userId 用户id
     * @return
     */
    boolean deleteAllAlarmUser(int userId);
}
