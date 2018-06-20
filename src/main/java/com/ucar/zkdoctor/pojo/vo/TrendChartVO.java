package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Description: 运行状态历史折线图表数据点
 * Created on 2018/1/26 11:08
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class TrendChartVO implements Serializable {

    private static final long serialVersionUID = 2147637421895156004L;

    /**
     * 时间点列表
     */
    private List<String> time;

    /**
     * 值列表
     */
    private List<String> value;

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }
}
