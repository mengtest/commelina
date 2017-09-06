package com.framework.message;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ResponseMessageDomain {

    private final int domain;
    private final ResponseMessage message;

    private ResponseMessageDomain(int domain, ResponseMessage message) {
        this.domain = domain;
        this.message = message;
    }

    public static ResponseMessageDomain newResponseMessageDomain(int domain, ResponseMessage message) {
        return new ResponseMessageDomain(domain, message);
    }

    public int getDomain() {
        return domain;
    }

    public ResponseMessage getMessage() {
        return message;
    }
}
