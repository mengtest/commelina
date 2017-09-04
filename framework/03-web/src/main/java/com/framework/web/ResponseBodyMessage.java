package com.framework.web;

import com.google.common.base.Preconditions;
import com.google.protobuf.Internal;

import java.io.Serializable;

/**
 * Created by @panyao on 2016/8/24.
 */
public final class ResponseBodyMessage<T extends Serializable> {

    private final int businessCode;
    private final long serverTimeMillis;
    private final T data;

    private static final int DEFAULT_SUCCESS = 0;
    static final int SERVER_ERROR = -1;

    static String getServerErrorJson() {
        return "{\"businessCode\":" + SERVER_ERROR + ",\"serverTimeMillis\":"
                + System.currentTimeMillis() + ",\"data\":\"unknown error.\"}";
    }

    private ResponseBodyMessage(int businessCode, T data) {
        this.businessCode = businessCode;
        this.serverTimeMillis = System.currentTimeMillis();
        this.data = data;
    }

    public static ResponseBodyMessage<String> error(Internal.EnumLite code) {
        Preconditions.checkArgument(code.getNumber() > 0);
        return success(code, null);
    }

    public static ResponseBodyMessage<String> success() {
        return new ResponseBodyMessage<String>(DEFAULT_SUCCESS, null);
    }

    public static <T extends Serializable> ResponseBodyMessage<T> success(T data) {
        return new ResponseBodyMessage<>(DEFAULT_SUCCESS, data);
    }

    public static <T extends Serializable> ResponseBodyMessage<T> success(Internal.EnumLite code, T data) {
        return new ResponseBodyMessage<>(code.getNumber(), data);
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public long getServerTimeMillis() {
        return serverTimeMillis;
    }

    public T getData() {
        return data;
    }

}