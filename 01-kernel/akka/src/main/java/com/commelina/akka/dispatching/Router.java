package com.commelina.akka.dispatching;

import com.google.protobuf.Internal;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public interface Router {

    /**
     * 获取路由id
     *
     * @return
     */
    Internal.EnumLite getRouterId();

}