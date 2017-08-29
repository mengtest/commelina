package com.nexus.maven.core.message;

import com.nexus.maven.core.AppVersion;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ApiRequest implements AppVersion {

    private final String apiName;
    private final String version;
    private final RequestArg[] args;

    protected ApiRequest(String apiName, String version, RequestArg[] args) {
        this.apiName = apiName;
        this.version = version;
        this.args = args;
    }

    public static ApiRequest newApiRequest(String apiName, String version, RequestArg[] args) {
        return new ApiRequest(apiName, version, args);
    }

    public String getVersion() {
        return this.version;
    }

    public String getApiName() {
        return this.apiName;
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