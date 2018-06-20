package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;

/**
 * Description: 分页控制
 * Created on 2017/12/26 16:08
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class PageVO implements Serializable {

    private static final long serialVersionUID = 6930724533908437030L;

    private Integer pageSize = 1;
    private Integer pageNum = 20;
    private Integer total = 1;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
