package com.ucar.zkdoctor.web;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Description: 后台处理结果
 * Created on 2017/12/26 10:17
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ConResult implements Serializable {

    private static final long serialVersionUID = -5957141240155159041L;

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 信息
     */
    private String message = "Successfully";

    /**
     * 结果数据
     */
    private Object data;

    public static ConResult success() {
        return success((Object) null);
    }

    public static ConResult success(Object data) {
        return new ConResult(true, (String) "Successfully", data);
    }

    public static ConResult success(Object data, String message) {
        return new ConResult(true, (String) message, data);
    }

    public static ConResult fail() {
        return fail("操作失败请重试!");
    }

    public static ConResult fail(String message) {
        if (StringUtils.isEmpty(message) || message.indexOf("Exception") > 0) {
            message = "系统操作过程中出现错误,请重试";
        }
        return new ConResult(false, message, (Object) null);
    }

    public ConResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public ConResult() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getData() {
        return this.data;
    }
}
