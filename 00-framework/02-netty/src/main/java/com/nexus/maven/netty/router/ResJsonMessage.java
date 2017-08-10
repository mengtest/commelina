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

    private ResJsonMessage(int businessCode, Object data) {
        this.businessCode = businessCode;
        this.data = data;
    }

    public static ResJsonMessage error(int code) {
        // 保存之后不能使用该方法产地消息，如果需要设置body值，需要使用 messageOk()
        ResJsonMessage resJsonMessage = new ResJsonMessage(code, null);
        return resJsonMessage;
    }

    public static ResJsonMessage success(Object _data) {
        return success(0, _data);
    }

    public static ResJsonMessage success(int code, Object _data) {
        return new ResJsonMessage(code, _data);
    }

}