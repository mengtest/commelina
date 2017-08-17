package com.nexus.maven.akka;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.nexus.maven.core.AppVersion;
import com.nexus.maven.core.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class AkkaRequest implements ApiRequest {

    private final String apiName;
    private final String version;
    private final Object[] args;

    private AkkaRequest(String apiName, String version, Object[] args) {
        this.apiName = apiName;
        this.version = version;
        this.args = args;
    }

    public static AkkaRequest newRequest(String apiName, Object... args) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(apiName));
        return new AkkaRequest(apiName, AppVersion.FIRST_VERSION, args);
    }

    public static AkkaRequest newRequest(String apiName, String version, Object... args) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(apiName));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(version));
        return new AkkaRequest(apiName, version, args);
    }

    public String getApiName() {
        return apiName;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getArg(int argName) {
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

    @Override
    public String getVersion() {
        return this.version;
    }
}
