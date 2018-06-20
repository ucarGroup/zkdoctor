package com.ucar.zkdoctor.dao.mysql;

import com.ucar.zkdoctor.pojo.po.ServiceLine;

import java.util.List;

/**
 * Description: 业务线Dao
 * Created on 2018/4/10 10:28
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ServiceLineDao {

    /**
     * 保存新业务线信息
     *
     * @param serviceLine 业务线信息
     * @return
     */
    boolean insertServiceLine(ServiceLine serviceLine);

    /**
     * 根据业务线名称获取业务线相关信息
     *
     * @param serviceLineName 业务线名称
     * @return
     */
    ServiceLine getServiceLineByName(String serviceLineName);

    /**
     * 获取所有业务线信息
     *
     * @return
     */
    List<ServiceLine> getAllServiceLine();

    /**
     * 更新某业务线信息
     *
     * @param serviceLine 业务线
     * @return
     */
    boolean updateServiceLine(ServiceLine serviceLine);

    /**
     * 删除某业务线信息
     *
     * @param serviceLineName 业务线名称
     * @return
     */
    boolean deleteServiceLineByName(String serviceLineName);

}
