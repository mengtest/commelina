package com.framework.message;

import com.framework.core.AppVersion;
import com.google.protobuf.Internal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
public final class ApiRequest extends ApiRequestForward implements AppVersion, Serializable {

    private long userId = 0;

    public ApiRequest(Internal.EnumLite opcode, String version, List<RequestArg> args) {
        super(opcode, version, args);
    }

    public ApiRequest setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getUserId() {
        return userId;
    }

}