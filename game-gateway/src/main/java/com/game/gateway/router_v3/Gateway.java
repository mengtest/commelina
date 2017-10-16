package com.game.gateway.router_v3;

import com.framework.akka_router.ApiRequest;
import com.framework.akka_router.DefaultLocalActorRequestHandler;
import com.framework.core.BusinessMessage;
import com.framework.core.DefaultMessageProvider;
import com.framework.core.MessageBody;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.proto.SocketASK;
import com.game.common.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_METHODS;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(forward = DOMAIN.GATE_WAY_VALUE)
public class Gateway extends DefaultLocalActorRequestHandler {

    private final MessageBody messageBody = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.GATEWAY_API_UNAUTHORIZED));

    @Override
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {

        switch (ask.getOpcode()) {
            // 登录接口允许匿名
            case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:
                return true;
        }

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

        return true;
    }

    //    @Override
//    protected boolean beforeHook(SocketASK ask, ChannelHandlerContext ctx) {
//        switch (ask.getOpcode()) {
//            // 登录接口允许匿名
//            case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:
//                return true;
//        }
//
//        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
//        if (userId <= 0) {
//            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, ask.getOpcode(), messageBody);
//            return false;
//        }
//
//        ask.setUserId(userId);
//
//        return true;
//    }

    @Override
    public final Internal.EnumLite getRouterId() {
        return DOMAIN.GATE_WAY;
    }

}