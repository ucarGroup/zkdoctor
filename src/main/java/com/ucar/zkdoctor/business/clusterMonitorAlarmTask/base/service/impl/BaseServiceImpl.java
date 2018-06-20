package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.service.impl;

import com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.service.BaseService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public abstract class BaseServiceImpl<T> implements BaseService<T> {

}