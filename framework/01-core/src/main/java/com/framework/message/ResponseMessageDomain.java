package com.framework.message;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/25.
 */
@Deprecated
public class ResponseMessageDomain {

    private final Internal.EnumLite domain;
    private final ResponseMessage message;

    private ResponseMessageDomain(Internal.EnumLite domain, ResponseMessage message) {
        this.domain = domain;
        this.message = message;
    }

    public static ResponseMessageDomain newMessage(Internal.EnumLite domain, ResponseMessage message) {
        return new ResponseMessageDomain(domain, message);
    }

    public Internal.EnumLite getDomain() {
        return domain;
    }

    public ResponseMessage getMessage() {
        return message;
    }
}
