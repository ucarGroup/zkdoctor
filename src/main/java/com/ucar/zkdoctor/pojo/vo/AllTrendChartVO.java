package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Description: 所有实例运行状态历史折线图表数据点
 * Created on 2018/1/26 20:52
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class AllTrendChartVO implements Serializable {

    private static final long serialVersionUID = 5872311797482426796L;

    /**
     * 时间点列表
     */
    private List<String> time;

    /**
     * 实例值列表
     */
    private Map<String, List<String>> value;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public Map<String, List<String>> getValue() {
        return value;
    }

    public void setValue(Map<String, List<String>> value) {
        this.value = value;
    }
}
