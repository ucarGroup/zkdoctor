package com.ucar.zkdoctor.service.system;

import com.ucar.zkdoctor.pojo.po.ServiceLine;

import java.util.List;

/**
 * Description: 业务线相关操作服务接口
 * Created on 2018/4/10 10:29
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public interface ServiceLineService {

    /**
     * 系统初始化的时候，初始化默认业务线配置
     */
    void initDefaultServiceLine();

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

    /**
     * 获取默认业务线
     *
     * @return
     */
    ServiceLine getDefaultServiceLine();
}
