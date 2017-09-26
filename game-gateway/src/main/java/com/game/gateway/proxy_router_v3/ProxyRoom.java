package com.game.gateway.proxy_router_v3;

import com.framework.akka_cluster_router.ClusterRouterJoinEntity;
import com.framework.akka_cluster_router.DefaultClusterActorRequestHandler;
import com.framework.message.ApiRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.game.gateway.MessageProvider;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(forward = DOMAIN.GAME_ROOM_VALUE)
public class ProxyRoom extends DefaultClusterActorRequestHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.GAME_ROOM;
    }

    @Override
    protected ClusterRouterJoinEntity beforeHook(ApiRequest request, ChannelHandlerContext ctx) {
        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ResponseMessage message = ResponseMessage.newMessage(
                    MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_UNAUTHORIZED)));

            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, request.getOpcode(), message);
            return null;
        }

        // matching server 必须登录才能访问
        return createNewJoinEntity(request, userId);
    }

}
