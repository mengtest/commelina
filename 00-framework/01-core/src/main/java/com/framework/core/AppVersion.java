package com.framework.core;

/**
 * Created by @panyao on 2017/8/11.
 */
public interface AppVersion {

    String FIRST_VERSION = "1.0.0";

    default String getVersion() {
        return FIRST_VERSION;
    }

}
