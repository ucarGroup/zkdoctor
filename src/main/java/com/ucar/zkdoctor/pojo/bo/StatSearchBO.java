package com.ucar.zkdoctor.pojo.bo;

import java.io.Serializable;

/**
 * Description: 统计数据查询BO
 * Created on 2018/1/26 10:42
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class StatSearchBO implements Serializable {

    private static final long serialVersionUID = -736420775948348233L;

    /**
     * id
     */
    private Integer id;

    /**
     * 开始时间
     */
    private String start;

    /**
     * 结束时间
     */
    private String end;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
