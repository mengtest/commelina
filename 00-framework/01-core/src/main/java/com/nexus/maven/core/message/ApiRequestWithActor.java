package com.nexus.maven.core.message;

import com.nexus.maven.core.AppVersion;

/**
 * Created by @panyao on 2017/8/25.
 */
public final class ApiRequestWithActor implements AppVersion {

    private final long userId;

    private final String apiMethod;
    private final String version;
    private final RequestArg[] args;

    public ApiRequestWithActor(long userId, String apiMethod, String version, RequestArg[] args) {
        this.userId = userId;
        this.apiMethod = apiMethod;
        this.version = version;
        this.args = args;
    }


    public static ApiRequestWithActor newApiRequestWithActor(long userId, String apiMethod, String version, RequestArg[] args) {
        return new ApiRequestWithActor(userId, apiMethod, version, args);
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

    public String getApiMethod() {
        return apiMethod;
    }

}
