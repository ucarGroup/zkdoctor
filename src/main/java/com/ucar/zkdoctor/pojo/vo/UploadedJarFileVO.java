package com.ucar.zkdoctor.pojo.vo;

import java.io.Serializable;

/**
 * Description: 已上传文件列表前端展示VO
 * Created on 2018/3/13 20:56
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class UploadedJarFileVO implements Serializable {

    private static final long serialVersionUID = 6351805454642352351L;

    /**
     * 表示文件id
     */
    private Integer uid;

    /**
     * 显示的文件名称
     */
    private String name;

    /**
     * 文件上传状态，done：已上传，error：上传失败
     */
    private String status;

    /**
     * 服务器响应信息
     */
    private String response;

    /**
     * 文件URL
     */
    private String url;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
