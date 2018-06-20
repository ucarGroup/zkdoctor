package com.ucar.zkdoctor.pojo.vo;

/**
 * Description: znode节点数据VO
 * Created on 2018/2/1 16:08
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ZnodeTreeNodeVO {

    /**
     * 节点结构示例：
     * + /zookeeper1
     *   + /node1
     *     + /zode11
     *   + /node2
     * + /zookeeper2
     *   + /node1
     *   + /node2
     */

    /**
     * znode节点名称，用于展示节点名字。如示例中：/zookeeper1、/node1等
     */
    private String title;

    /**
     * 唯一识别该节点的key，此处用该节点path来表示。如示例中：/zookeeper1，/zookeeper1/node1等
     */
    private String key;

    /**
     * 该节点是否为叶子节点，默认为非叶子节点，能够展开
     */
    private Boolean isLeaf = false;

    @Override
    public String toString() {
        return "ZnodeTreeNodeVO{" +
                "title='" + title + '\'' +
                ", key='" + key + '\'' +
                ", isLeaf=" + isLeaf +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getLeaf() {
        return isLeaf;
    }

    public void setLeaf(Boolean leaf) {
        isLeaf = leaf;
    }
}
