package com.ucar.zkdoctor.service.system.impl;

import com.ucar.zkdoctor.dao.mysql.ServiceLineDao;
import com.ucar.zkdoctor.pojo.po.ServiceLine;
import com.ucar.zkdoctor.service.system.ServiceLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description: 业务线相关操作服务接口实现类
 * Created on 2018/4/10 10:29
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
@Service("serviceLineService")
public class ServiceLineServiceImpl implements ServiceLineService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLineServiceImpl.class);

    /**
     * 默认业务线信息
     */
    private static final String DEFAULT_SERVICE_LINE = "default";

    @Resource
    private ServiceLineDao serviceLineDao;

    @Override
    public void initDefaultServiceLine() {
        if (serviceLineDao.getServiceLineByName(DEFAULT_SERVICE_LINE) == null) {
            ServiceLine serviceLine = new ServiceLine();
            serviceLine.setServiceLineName(DEFAULT_SERVICE_LINE);
            serviceLine.setServiceLineDesc("默认");
            boolean insertDefault = insertServiceLine(serviceLine);
            LOGGER.info("Init default service line default to DB: {}.", insertDefault);
        } else {
            LOGGER.info("Init default service line default to DB, but it is already in DB.");
        }
    }

    @Override
    public boolean insertServiceLine(ServiceLine serviceLine) {
        if (serviceLine == null) {
            return false;
        }
        return serviceLineDao.insertServiceLine(serviceLine);
    }

    @Override
    public ServiceLine getServiceLineByName(String serviceLineName) {
        return serviceLineDao.getServiceLineByName(serviceLineName);
    }

    @Override
    public List<ServiceLine> getAllServiceLine() {
        return serviceLineDao.getAllServiceLine();
    }

    @Override
    public boolean updateServiceLine(ServiceLine serviceLine) {
        return serviceLineDao.updateServiceLine(serviceLine);
    }

    @Override
    public boolean deleteServiceLineByName(String serviceLineName) {
        return serviceLineDao.deleteServiceLineByName(serviceLineName);
    }

    @Override
    public ServiceLine getDefaultServiceLine() {
        // 如果有设置默认的信息，走默认，否则直接返回默认
        ServiceLine serviceLine = serviceLineDao.getServiceLineByName(DEFAULT_SERVICE_LINE);
        if (serviceLine == null) {
            serviceLine = new ServiceLine();
            serviceLine.setId(0);
            serviceLine.setServiceLineName(DEFAULT_SERVICE_LINE);
            serviceLine.setServiceLineDesc("默认");
        }
        return serviceLine;
    }
}
