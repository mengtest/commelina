package org.commelina.match24.play.gateway.portal;

import org.commelina.akka.dispatching.DefaultClusterActorRequestHandler;
import org.commelina.akka.dispatching.local.AkkaLocalWorkerSystem;
import org.commelina.akka.dispatching.proto.ApiRequest;
import org.commelina.core.BusinessMessage;
import org.commelina.core.DefaultMessageProvider;
import org.commelina.core.MessageBody;
import org.commelina.example.game.common.proto.DOMAIN;
import org.commelina.match24.play.gateway.proto.ERROR_CODE;
import org.commelina.niosocket.ContextAdapter;
import org.commelina.niosocket.NioSocketRouter;
import org.commelina.niosocket.ReplyUtils;
import org.commelina.niosocket.proto.SocketASK;
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
