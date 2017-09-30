package com.game.gateway.router_v3;

import com.framework.akka_router.DefaultLocalActorRequestHandler;
import com.framework.message.ApiRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.DefaultMessageProvider;
import com.framework.message.MessageBus;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_METHODS;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;


/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(forward = DOMAIN.GATE_WAY_VALUE)
public final class Gateway extends DefaultLocalActorRequestHandler {

    private final MessageBus messageBus = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.GATEWAY_API_UNAUTHORIZED));

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.GATE_WAY;
    }

    @Override
    protected boolean beforeHook(ApiRequest request, ChannelHandlerContext ctx) {
        switch (request.getOpcode().getNumber()) {
            // 登录接口允许匿名
            case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:
                return true;
        }

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, request.getOpcode(), messageBus);
            return false;
        }

        return true;
    }

}