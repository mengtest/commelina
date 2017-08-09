package com.game.foundation.gateway.api;

import com.game.foundation.netty.router.RpcApi;
import com.game.foundation.netty.router.RpcMethod;

/**
 * Created by @panyao on 2017/8/8.
 */
@RpcApi
public class Passport {

    @RpcMethod(value = "connect", version = "1.0.1")
    public void connect(String token) {
        // 去 token service 验证 token 是否有效
    }

}