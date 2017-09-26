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

    /**
     * 要截取的数组数量 [1,2,3]
     * subArg(1) -> [2,3]
     * subArg(2) -> [3]
     * subArg(3) -> []
     * subArg(4) -> error
     *
     * @param subSize
     * @return
     */
    public RequestArg[] subArg(int subSize) {
        if (args == null || args.length < 1) {
            return null;
        }
        RequestArg[] args = new RequestArg[this.getArgs().length - subSize];
        for (int i = subSize; i < this.getArgs().length; i++) {
            args[i - subSize] = this.getArgs()[i];
        }
        return args;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }

}
