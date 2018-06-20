package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.service;

import java.io.Serializable;

public interface BaseService<T> {
    T getById(Serializable id);

    void save(T entity);

    void delete(Serializable id);
}