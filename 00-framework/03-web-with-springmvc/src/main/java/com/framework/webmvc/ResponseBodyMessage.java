package com.framework.webmvc;

import com.google.common.base.Preconditions;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2016/8/24.
 *
 * @author panyao
 * @coding.net https://coding.net/u/pandaxia
 * @github https://github.com/freedompy
 */
public final class ResponseBodyMessage {

    private final int businessCode;
    private final long serverTimeMillis;
    private final Object data;

    static final String DEFAULT_DATA = null;
    static final int DEFAULT_SUCCESS = 0;
    public static final int SERVER_ERROR = -1;

    private ResponseBodyMessage(int businessCode, Object data) {
        this.businessCode = businessCode;
        this.serverTimeMillis = System.currentTimeMillis();
        this.data = data;
    }

    public static ResponseBodyMessage error(Internal.EnumLite code) {
        Preconditions.checkArgument(code.getNumber() > 0);
        return success(code, DEFAULT_DATA);
    }

    public static ResponseBodyMessage success() {
        return new ResponseBodyMessage(DEFAULT_SUCCESS, DEFAULT_DATA);
    }

    public static ResponseBodyMessage success(Object data) {
        return new ResponseBodyMessage(DEFAULT_SUCCESS, data);
    }

    public static ResponseBodyMessage success(Internal.EnumLite code, Object data) {
        return new ResponseBodyMessage(code.getNumber(), data);
    }

}