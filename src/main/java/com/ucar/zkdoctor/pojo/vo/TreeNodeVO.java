package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 菜单节点信息
 * Created on 2017/12/26 15:00
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class TreeNodeVO implements Serializable {

    private static final long serialVersionUID = -4272709639058958936L;

    /**
     * 唯一识别该节点的key
     */
    private String key;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 对应的请求的URL
     */
    private String url;

    /**
     * 图标设置
     */
    private String extra;

    /**
     * 顺序
     */
    private int order;

    /**
     * 子节点信息
     */
    private List<TreeNodeVO> children = new ArrayList();

    public TreeNodeVO() {
    }

    public TreeNodeVO(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public List<TreeNodeVO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNodeVO> children) {
        this.children = children;
    }
}
