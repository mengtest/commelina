package com.commelina.math24.play.gateway.portal;

import com.commelina.akka.dispatching.DefaultClusterActorRequestHandler;
import com.commelina.akka.dispatching.local.AkkaLocalWorkerSystem;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.core.BusinessMessage;
import com.commelina.core.DefaultMessageProvider;
import com.commelina.core.MessageBody;
import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.gateway.proto.ERROR_CODE;
import com.commelina.niosocket.ContextAdapter;
import com.commelina.niosocket.NioSocketRouter;
import com.commelina.niosocket.ReplyUtils;
import com.commelina.niosocket.proto.SocketASK;
import com.game.gateway.proto.ChangeAccesssDoamin;
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
