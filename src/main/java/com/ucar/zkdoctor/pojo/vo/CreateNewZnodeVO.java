package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;

/**
 * Description: 新增节点VO
 * Created on 2018/2/5 12:56
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class CreateNewZnodeVO implements Serializable {

    private static final long serialVersionUID = 2263708865366377444L;

    /**
     * 父节点路径，新增节点路径为：parentPath/childPath
     */
    private String parentPath;

    /**
     * 新建子节点路径
     */
    private String childPath;

    /**
     * 节点数据
     */
    private String data;

    /**
     * 是否创建不存在的父节点，true：创建，false：不创建，如果某个父节点不存在，则创建节点失败
     */
    private Boolean createParentNeeded;

    /**
     * 节点ACL设置，取自ZooDefs.Ids
     * 0: OPEN_ACL_UNSAFE = new ArrayList(Collections.singletonList(new ACL(31, ANYONE_ID_UNSAFE)));
     * 1: CREATOR_ALL_ACL = new ArrayList(Collections.singletonList(new ACL(31, AUTH_IDS)));
     * 2：READ_ACL_UNSAFE = new ArrayList(Collections.singletonList(new ACL(1, ANYONE_ID_UNSAFE)));
     */
    private String acl;

    @Override
    public String toString() {
        return "CreateNewZnodeVO{" +
                "parentPath='" + parentPath + '\'' +
                ", childPath='" + childPath + '\'' +
                ", data='" + data + '\'' +
                ", createParentNeeded=" + createParentNeeded +
                ", acl='" + acl + '\'' +
                '}';
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getChildPath() {
        return childPath;
    }

    public void setChildPath(String childPath) {
        this.childPath = childPath;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getCreateParentNeeded() {
        return createParentNeeded;
    }

    public void setCreateParentNeeded(Boolean createParentNeeded) {
        this.createParentNeeded = createParentNeeded;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }
}
