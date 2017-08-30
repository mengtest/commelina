package com.nexus.maven.core.message;

import com.nexus.maven.core.AppVersion;

/**
 * Created by @panyao on 2017/8/15.
 */
public interface MessageBus extends AppVersion {

    enum BusinessProtocol {
        JSON
    }

    BusinessProtocol getBp();

    byte[] getBytes();

}
