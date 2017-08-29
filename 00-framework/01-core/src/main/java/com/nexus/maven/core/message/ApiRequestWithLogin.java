package com.nexus.maven.core.message;

import com.nexus.maven.core.AppVersion;

/**
 * Created by @panyao on 2017/8/25.
 */
public final class ApiRequestWithLogin extends ApiRequest implements AppVersion {

    private final long userId;

    private ApiRequestWithLogin(String apiName, String version, long userId, RequestArg[] args) {
        super(apiName, version, args);
        this.userId = userId;
    }

    public static ApiRequestWithLogin newInstance(String apiName, String version, long userId, RequestArg[] args) {
        return new ApiRequestWithLogin(apiName, version, userId, args);
    }

    public long getUserId() {
        return userId;
    }

}
