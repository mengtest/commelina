package com.business.game.gateway.portal;

import com.business.game.message.common.proto.DOMAIN;
import com.game.gateway.proto.ChangeAccesssDoamin;
import com.game.gateway.proto.ERROR_CODE;
import com.github.freedompy.commelina.akka.dispatching.DefaultClusterActorRequestHandler;
import com.github.freedompy.commelina.akka.dispatching.local.AkkaLocalWorkerSystem;
import com.github.freedompy.commelina.akka.dispatching.proto.ApiRequest;
import com.github.freedompy.commelina.core.BusinessMessage;
import com.github.freedompy.commelina.core.DefaultMessageProvider;
import com.github.freedompy.commelina.core.MessageBody;
import com.github.freedompy.commelina.niosocket.ContextAdapter;
import com.github.freedompy.commelina.niosocket.NioSocketRouter;
import com.github.freedompy.commelina.niosocket.ReplyUtils;
import com.github.freedompy.commelina.niosocket.proto.SocketASK;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author @panyao
 * @date 2017/9/22
 */
@NioSocketRouter(forward = DOMAIN.MATCHING_VALUE)
public class ProxyMatching extends DefaultClusterActorRequestHandler {

    private final MessageBody messageBody = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.MATCHING_API_UNAUTHORIZED));

    @Override
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {
        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ReplyUtils.reply(ctx, DOMAIN.GATEWAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

        //
        AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(
                ChangeAccesssDoamin.newBuilder()
                        .setUserId(userId)
                        .setDomain(DOMAIN.MATCHING)
                        .build()
        );

        return true;
    }

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.MATCHING;
    }

}
