package com.game.gateway.proxy_router_v3;

import com.framework.message.ApiRequest;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.RequestHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(apiPathCode = com.game.gateway.proto.GATEWAY_APIS.GAME_ROOM_V1_0_0_VALUE)
public class ProxyRoom implements RequestHandler {

    @Override
    public void onRequest(ApiRequest request, ChannelHandlerContext outputHandler) {

    }

}
