package com.nexus.maven.akka;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class ResponseDomain {

    private int code;
    private Object msg;

    public static ResponseDomain success() {
        ResponseDomain domain = new ResponseDomain();
        domain.code = 0;
        return domain;
    }

    public static ResponseDomain success(int code) {
        ResponseDomain domain = new ResponseDomain();
        domain.code = code;
        return domain;
    }

    public static ResponseDomain success(int code, Object msg) {
        ResponseDomain domain = new ResponseDomain();
        domain.code = code;
        domain.msg = msg;
        return domain;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

}
