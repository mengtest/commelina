package com.framework.message;

import com.framework.core.AppVersion;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/25.
 */
public final class ApiLoginRequest implements AppVersion {

    private final long userId;

    private final Internal.EnumLite apiOpcode;
    private final String version;
    private final RequestArg[] args;

    private ApiLoginRequest(long userId, Internal.EnumLite apiOpcode, String version, RequestArg[] args) {
        this.userId = userId;
        this.apiOpcode = apiOpcode;
        this.version = version;
        this.args = args;
    }

    public static ApiLoginRequest newClientApiRequestWithActor(long userId, Internal.EnumLite apiOpcode, String version, RequestArg[] args) {
        return new ApiLoginRequest(userId, apiOpcode, version, args);
    }

    public static ApiLoginRequest newServerApiRequestWithActor(long userId, Internal.EnumLite apiOpcode, String version, RequestArg[] args) {
        return new ApiLoginRequest(userId, apiOpcode, version, args);
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

    public Internal.EnumLite getApiOpcode() {
        return apiOpcode;
    }

}
