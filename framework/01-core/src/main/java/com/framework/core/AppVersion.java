package com.framework.core;

/**
 * 版本约束
 *
 * @author @panyao
 * @date 2017/8/11
 */
public interface AppVersion {

    String FIRST_VERSION = "1.0.0";

    /**
     * 获取当前的版本号，默认为 1.0.0
     *
     * @return
     */
    default String getVersion() {
        return FIRST_VERSION;
    }

}
