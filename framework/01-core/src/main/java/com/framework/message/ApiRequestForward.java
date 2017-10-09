package com.framework.message;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/15.
 */
public class ApiRequestForward {

    private final Internal.EnumLite opcode;
    private final String version;
    private final RequestArg[] args;

    private ApiRequestForward(Internal.EnumLite opcode, String version, RequestArg[] args) {
        this.opcode = opcode;
        this.version = version;
        this.args = args;
    }

    public static ApiRequestForward newRequest(Internal.EnumLite opcode, String version, RequestArg[] args) {
        return new ApiRequestForward(opcode, version, args);
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }

    public String getVersion() {
        return this.version;
    }

    public RequestArg[] getArgs() {
        return this.args;
    }

    public RequestArg getArg(int argName) {
        try {
            return args[argName];
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

}
