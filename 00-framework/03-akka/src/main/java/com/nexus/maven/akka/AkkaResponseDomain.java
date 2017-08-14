package com.nexus.maven.akka;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class AkkaResponseDomain {

    private int code;
    private Object msg;

    public static AkkaResponseDomain success() {
        AkkaResponseDomain domain = new AkkaResponseDomain();
        domain.code = 0;
        return domain;
    }

    public static AkkaResponseDomain success(int code) {
        AkkaResponseDomain domain = new AkkaResponseDomain();
        domain.code = code;
        return domain;
    }

    public static AkkaResponseDomain success(int code, Object msg) {
        AkkaResponseDomain domain = new AkkaResponseDomain();
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
