package com.framework.utils;

import com.google.protobuf.Internal;

/**
 * Created by panyao on 2017/9/2.
 */
public final class ServiceDomainMessage<T> {

    private final Internal.EnumLite errorCode;
    private final T data;

    private ServiceDomainMessage(Internal.EnumLite errorCode, T data) {
        this.errorCode = errorCode;
        this.data = data;
    }

    public static <T> ServiceDomainMessage<T> newMessage(T t) {
        return new ServiceDomainMessage<>(() -> 0, t);
    }

    public static <T> ServiceDomainMessage<T> newMessage(Internal.EnumLite errorCode, T t) {
        return new ServiceDomainMessage<>(errorCode, t);
    }

    public static <T> ServiceDomainMessage<T> newMessage(Internal.EnumLite errorCode) {
        return newMessage(errorCode, null);
    }

    public Internal.EnumLite getErrorCode() {
        return errorCode;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return this.errorCode.getNumber() == 0;
    }

}
