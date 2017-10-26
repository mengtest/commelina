package com.game.gateway.router;

import com.github.freedompy.commelina.akka.dispatching.DefaultLocalActorRequestHandler;
import com.github.freedompy.commelina.akka.dispatching.proto.ApiRequest;
import com.github.freedompy.commelina.core.BusinessMessage;
import com.github.freedompy.commelina.core.DefaultMessageProvider;
import com.github.freedompy.commelina.core.MessageBody;
import com.github.freedompy.commelina.niosocket.ContextAdapter;
import com.github.freedompy.commelina.niosocket.NioSocketRouter;
import com.github.freedompy.commelina.niosocket.ReplyUtils;
import com.github.freedompy.commelina.niosocket.proto.SocketASK;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_METHODS;
import com.google.protobuf.Internal;
import com.message.common.proto.DOMAIN;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author @panyao
 * @date 2017/9/22
 */
@NioSocketRouter(forward = DOMAIN.GATEWAY_VALUE)
public class Gateway extends DefaultLocalActorRequestHandler {

    private final MessageBody messageBody = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.GATEWAY_API_UNAUTHORIZED));

    @Override
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {

        switch (ask.getOpcode()) {
            // 登录接口允许匿名
            case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:
                return true;
            default:

        }

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ReplyUtils.reply(ctx, DOMAIN.GATEWAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

        return true;
    }

    @Override
    public final Internal.EnumLite getRouterId() {
        return DOMAIN.GATEWAY;
    }

}