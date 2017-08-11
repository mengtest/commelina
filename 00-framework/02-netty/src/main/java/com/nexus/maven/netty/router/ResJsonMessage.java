package com.nexus.maven.netty.router;

import java.io.Serializable;

/**
 * Created by @panyao on 2016/8/24.
 *
 * @author panyao
 * @coding.net https://coding.net/u/pandaxia
 * @github https://github.com/freedompy
 */
public final class ResJsonMessage implements Serializable {

    private final int businessCode;
    private final Object data;

    public static final String DEFAULT_DATA = null;
    public static final int DEFAULT_SUCCESS = 0;

    private ResJsonMessage(int businessCode, Object data) {
        this.businessCode = businessCode;
        this.data = data;
    }

    public static ResJsonMessage error(int code) {
        return success(code, DEFAULT_DATA);
    }

    public static ResJsonMessage success() {
        return success(DEFAULT_SUCCESS, DEFAULT_DATA);
    }

    public static ResJsonMessage success(Object data) {
        return success(DEFAULT_SUCCESS, data);
    }

    public static ResJsonMessage success(int code, Object data) {
        return new ResJsonMessage(code, data);
    }

}