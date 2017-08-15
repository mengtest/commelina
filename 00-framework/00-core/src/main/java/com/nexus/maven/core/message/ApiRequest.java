package com.nexus.maven.core.message;

import com.nexus.maven.core.AppVersion;

/**
 * Created by @panyao on 2017/8/15.
 */
public interface ApiRequest extends AppVersion {

    String getApiName();

    Object[] getArgs();

    Object getArg(int argName);

}
