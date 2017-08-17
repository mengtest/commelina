package com.nexus.maven.akka;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.nexus.maven.core.AppVersion;
import com.nexus.maven.core.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class AkkaRequest implements ApiRequest {

    private String apiName;
    private String version;
    private Object[] args;

    public static AkkaRequest newRequest(String apiName, Object... args) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(apiName));
        AkkaRequest request = new AkkaRequest();
        request.setApiName(apiName);
        request.setVersion(AppVersion.FIRST_VERSION);
        request.setArgs(args);
        return request;
    }

    public static AkkaRequest newRequest(String apiName, String version, Object... args) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(apiName));
        Preconditions.checkArgument(!Strings.isNullOrEmpty(version));
        AkkaRequest request = new AkkaRequest();
        request.setApiName(apiName);
        request.setVersion(version);
        request.setArgs(args);
        return request;
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

    void setApiName(String apiName) {
        this.apiName = apiName;
    }

    void setVersion(String version) {
        this.version = version;
    }

    void setArgs(Object[] args) {
        this.args = args;
    }

}
