package com.nexus.maven.netty.socket;

/**
 * Created by @panyao on 2017/8/11.
 */
public interface MessageVersion {

    String FIRST_VERSION = "1.0.0";

    default String getVersion() {
        return FIRST_VERSION;
    }

}
