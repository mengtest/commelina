package com.commelina.core;

import com.google.protobuf.Internal;

/**
 * 业务消息实体
 *
 * @author @panyao
 * @date 2016/8/24
 */
@Deprecated
public final class BusinessMessage<T> {

    private final int businessCode;
    private final T data;

    private static final BusinessMessage<String> EMPTY_SUCCESS = new BusinessMessage<>(0, null);

    private BusinessMessage(int businessCode, T data) {
        this.businessCode = businessCode;
        this.data = data;
    }

    public static BusinessMessage<String> error(Internal.EnumLite code) {
        return success(code, null);
    }

    public static BusinessMessage<String> success() {
        return EMPTY_SUCCESS;
    }

    public static <T> BusinessMessage<T> success(T data) {
        return new BusinessMessage<>(0, data);
    }

    public static <T> BusinessMessage<T> success(Internal.EnumLite code, T data) {
        return new BusinessMessage<>(code.getNumber(), data);
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public T getData() {
        return data;
    }

}