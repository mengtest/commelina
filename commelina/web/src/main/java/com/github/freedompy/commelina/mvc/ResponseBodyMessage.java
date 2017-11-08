package com.github.freedompy.commelina.mvc;

import com.google.common.base.Preconditions;
import com.google.protobuf.Internal;

import java.io.Serializable;

/**
 * @author @panyao
 * @date 2016/8/24
 */
public final class ResponseBodyMessage<T extends Serializable> {

    private final int businessCode;
    String businessMsg;
    final long serverTimeMillis;
    final T data;

    private static final int DEFAULT_SUCCESS = 0;
    static final int SERVER_ERROR = -1;
    static final String SERVER_ERROR_STR = SERVER_ERROR + "";

    static String getServerErrorJson(String msg) {
        return "{\"businessCode\":" + SERVER_ERROR + "," +
                "\"businessMsg\":\"" + msg + "\"," +
                "\"serverTimeMillis\":" + System.currentTimeMillis() + "," +
                "\"data\":null}";
    }

    private ResponseBodyMessage(int businessCode, T data) {
        this.businessCode = businessCode;
        this.data = data;
        this.serverTimeMillis = System.currentTimeMillis();
    }

    public static ResponseBodyMessage<String> error(Internal.EnumLite code) {
        Preconditions.checkArgument(code.getNumber() > 0);
        return success(code, null);
    }

    public static ResponseBodyMessage<String> success() {
        return new ResponseBodyMessage<>(DEFAULT_SUCCESS, null);
    }

    public static <T extends Serializable> ResponseBodyMessage<T> success(T data) {
        return new ResponseBodyMessage<>(DEFAULT_SUCCESS, data);
    }

    public static <T extends Serializable> ResponseBodyMessage<T> success(Internal.EnumLite code, T data) {
        return new ResponseBodyMessage<>(code.getNumber(), data);
    }

    public Integer getBusinessCode() {
        return businessCode;
    }

}