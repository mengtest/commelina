package com.framework.message;

import com.framework.core.AppVersion;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/25.
 */
public final class ApiRequest implements AppVersion {

    private long userId = 0;

    private final Internal.EnumLite opcode;
    private final String version;
    private final RequestArg[] args;

    private ApiRequest(Internal.EnumLite opcode, String version, RequestArg[] args) {
        this.opcode = opcode;
        this.version = version;
        this.args = args;
    }

    public static ApiRequest newRequest(Internal.EnumLite apiOpcode, String version, RequestArg[] args) {
        return new ApiRequest(apiOpcode, version, args);
    }

    public ApiRequest setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String getVersion() {
        return version;
    }

    public RequestArg[] getArgs() {
        return args;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }

    public RequestArg getArg(int argName) {
        try {
            return args[argName];
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

}