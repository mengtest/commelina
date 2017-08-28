package com.nexus.maven.core.message;

import com.google.common.base.Strings;
import com.nexus.maven.core.AppVersion;

/**
 * Created by @panyao on 2017/8/25.
 */
public final class AkkaActorApiRequest implements AppVersion {

    private final String apiName;
    private final String version;
    private final long userId;
    private final RequestArg[] args;

    public AkkaActorApiRequest(String apiName, String version, long userId, RequestArg[] args) {
        this.apiName = apiName;
        this.version = version;
        this.userId = userId;
        this.args = args;
    }

    public String getVersion() {
        return Strings.isNullOrEmpty(this.version) ? AppVersion.FIRST_VERSION : this.version;
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
        for (int i = 0; i < args.length; i++) {
            if (i == argName) {
                return args[i];
            }
        }
        return null;
    }

    public long getUserId() {
        return userId;
    }

}
