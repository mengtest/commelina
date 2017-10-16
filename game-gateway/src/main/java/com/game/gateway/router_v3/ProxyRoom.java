package com.game.gateway.router_v3;

import com.framework.akka_router.ApiRequest;
import com.framework.akka_router.DefaultClusterActorRequestHandler;
import com.framework.core.BusinessMessage;
import com.framework.core.DefaultMessageProvider;
import com.framework.core.MessageBody;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.proto.SocketASK;
import com.game.common.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(forward = DOMAIN.GAME_ROOM_VALUE)
public class ProxyRoom extends DefaultClusterActorRequestHandler {

    private final MessageBody messageBody = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_UNAUTHORIZED));

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.GAME_ROOM;
    }

    @Override
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {
        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

        return true;
    }

}
