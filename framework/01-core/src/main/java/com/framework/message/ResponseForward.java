package com.framework.message;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/15.
 */
@Deprecated
public class ResponseForward {

    private final Internal.EnumLite toDomain;
    private final Internal.EnumLite apiOpcode;
    private final String version;
    private final RequestArg[] args;

    private ResponseForward(Internal.EnumLite toDomain, Internal.EnumLite apiOpcode, String version, RequestArg[] args) {
        this.toDomain = toDomain;
        this.apiOpcode = apiOpcode;
        this.version = version;
        this.args = args;
    }

    public static ResponseForward newApiRequestForward(Internal.EnumLite domain, Internal.EnumLite apiMethod, String version, RequestArg[] args) {
        return new ResponseForward(domain, apiMethod, version, args);
    }

    public Internal.EnumLite getApiOpcode() {
        return apiOpcode;
    }

    public Internal.EnumLite getToDomain() {
        return toDomain;
    }

    public String getVersion() {
        return this.version;
    }

    public RequestArg[] getArgs() {
        return this.args;
    }

    public RequestArg getArg(int argName) {
        if (args == null || args.length == 0) {
            return null;
        }

        try {
            return args[argName];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public RequestArg[] subArg(int subSize) {
        if (args == null || args.length < 2) {
            return null;
        }
        RequestArg[] args = new RequestArg[this.getArgs().length - subSize];
        for (int i = subSize; i < this.getArgs().length; i++) {
            args[i - subSize] = this.getArgs()[i];
        }
        return args;
    }
}
