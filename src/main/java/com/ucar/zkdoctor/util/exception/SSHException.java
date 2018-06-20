package com.ucar.zkdoctor.util.exception;

/**
 * Description: SSH异常
 * Created on 2018/1/23 12:42
 *
 * @author 吕小玲(xiaoling.lv@ucarinc.com)
 */
public class SSHException extends Exception {

    private static final long serialVersionUID = -3690754092850349309L;

    public SSHException() {
        super();
    }

    public SSHException(String message) {
        super(message);
    }

    public SSHException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSHException(Throwable cause) {
        super(cause);
    }
}

