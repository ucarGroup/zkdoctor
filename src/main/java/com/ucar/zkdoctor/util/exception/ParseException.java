package com.ucar.zkdoctor.util.exception;

/**
 * Description: 数据转化异常
 * Created on 2018/1/23 11:29
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class ParseException extends Exception {

    private static final long serialVersionUID = -2149749726436911783L;

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

