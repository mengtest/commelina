package com.github.freedompy.commelina.utils;

import com.google.protobuf.Internal;

/**
 * service 返回的domain 实体
 *
 * @author panyao
 * @date 2017/9/2
 */
public final class ServiceDomainEmptyMessage {

    private final Internal.EnumLite errorCode;

    private static final ServiceDomainEmptyMessage SUCCESS = new ServiceDomainEmptyMessage(() -> 0);

    private ServiceDomainEmptyMessage(Internal.EnumLite errorCode) {
        this.errorCode = errorCode;
    }

    public static ServiceDomainEmptyMessage newMessage() {
        return SUCCESS;
    }

    public static ServiceDomainEmptyMessage newMessage(Internal.EnumLite errorCode) {
        return newMessage(errorCode);
    }

    public Internal.EnumLite getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return this.errorCode.getNumber() == 0;
    }

}
