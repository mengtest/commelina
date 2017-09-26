package com.game.gateway.router_v3;

import com.framework.akka_router.DefaultClusterActorRequestHandler;
import com.framework.message.ApiRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.DefaultMessageProvider;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(forward = DOMAIN.MATCHING_VALUE)
public class ProxyMatching extends DefaultClusterActorRequestHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.MATCHING;
    }

    @Override
    public void onRequest(ApiRequest request, ChannelHandlerContext ctx) {
        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ResponseMessage message = ResponseMessage.newMessage(
                    DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.MATCHING_API_UNAUTHORIZED)));

            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, request.getOpcode(), message);
            return;
        }

        request.setUserId(userId);
        super.onRequest(request, ctx);
    }

}
