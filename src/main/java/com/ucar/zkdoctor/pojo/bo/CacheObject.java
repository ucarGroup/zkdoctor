package com.ucar.zkdoctor.pojo.bo;

import java.util.Date;

/**
 * Description: 本地缓存对象类
 * Created on 2018/1/31 11:19
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CacheObject<T> {

    /**
     * 需要缓存的对象
     */
    private T object;

    /**
     * 缓存时长，超过该时长，需要重新再获取数据进行覆盖
     */
    private long time;

    public CacheObject(T object, long time) {
        this.object = object;
        this.time = time;
    }

    public T getObject() {
        return object;
    }

    /**
     * 是否过期
     *
     * @param second 过期时长
     * @return
     */
    public boolean expired(int second) {
        if (second <= 0) {
            return false;
        } else {
            return (new Date().getTime() - time) > second * 1000;
        }
    }
}
