package com.commelina.match24.play.robot.message;

import com.google.common.base.Preconditions;
import com.google.protobuf.Internal;

/**
 *
 * @author @panyao
 * @date 2016/8/24
 */
public final class BusinessMessage<T> {

    private final int businessCode;
    private final T data;

    static final String DEFAULT_DATA = null;
    static final int DEFAULT_SUCCESS = 0;

    private BusinessMessage(int businessCode, T data) {
        this.businessCode = businessCode;
        this.data = data;
    }

    public static BusinessMessage<String> error(Internal.EnumLite code) {
        Preconditions.checkArgument(code.getNumber() > 0);
        return success(code, DEFAULT_DATA);
    }

    public static BusinessMessage<String> success() {
        return new BusinessMessage<>(DEFAULT_SUCCESS, DEFAULT_DATA);
    }

    public static <T> BusinessMessage<T> success(T data) {
        return new BusinessMessage<>(DEFAULT_SUCCESS, data);
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