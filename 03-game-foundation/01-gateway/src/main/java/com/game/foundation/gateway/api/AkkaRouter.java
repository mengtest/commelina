package com.game.foundation.gateway.api;

import com.nexus.maven.netty.socket.router.RpcApi;
import com.nexus.maven.netty.socket.router.RpcMethod;

/**
 * Created by @panyao on 2017/8/9.
 */
@RpcApi
public class AkkaRouter {

    @RpcMethod(value = "routing")
    public void routing() {

    }

}
