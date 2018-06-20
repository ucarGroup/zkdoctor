package com.ucar.zkdoctor.business.clusterMonitorAlarmTask.base.dao;

import java.io.Serializable;

public interface BaseDao<T> {
    T getById(Serializable id);

    void insert(T entity);

    void update(T entity);

    void delete(Serializable id);
}