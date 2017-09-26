package com.game.gateway.portal;

import akka.actor.Props;
import com.framework.message.*;
import com.framework.nio_akka.ActorRequestRemoteProxyWatching;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.game.gateway.AkkaRemoteActorEntity;
import com.game.gateway.MessageProvider;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_APIS;
import io.netty.channel.ChannelHandlerContext;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/8/25.
 */
@NioSocketRouter(forward = GATEWAY_APIS.MATCHING_V1_0_0_VALUE)
@Deprecated
public class MatchingRequestHandler  {

    @Resource
    private AkkaRemoteActorEntity akkaRemoteActorEntity;

    public Props getProps(ChannelHandlerContext outputHandler) {
        return ActorRequestRemoteProxyWatching.props(
                MatchingRemoteProxyRouterActorRequestRequestRemoteProxy.class,
                DOMAIN.MATCHING_VALUE,
                akkaRemoteActorEntity.getMatchingRequestPath(),
                outputHandler
        );
    }

    private static class MatchingRemoteProxyRouterActorRequestRequestRemoteProxy extends ActorRequestRemoteProxyWatching {

        public MatchingRemoteProxyRouterActorRequestRequestRemoteProxy(int domain, String remotePath, ChannelHandlerContext context) {
            super(domain, remotePath, context);
        }

        @Override
        public void onRequest(ApiRequest request) {
            long userId = ContextAdapter.getLoginUserId(context.channel().id());
            if (userId <= 0) {
                ResponseMessage message = ResponseMessage.newMessage(
                        MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.MATCHING_API_UNAUTHORIZED)));

                ResponseMessageDomain messageDomain = ResponseMessageDomain.newMessage(DOMAIN.MATCHING, message);

                // 回复消息到 gateway domain
                getSelf().tell(messageDomain, getSelf());
                return;
            }
            getSelf().tell(ApiRequestLogin.newRequest(userId, request.getOpcode(), request.getVersion(), request.getArgs()), getSelf());
        }

    }

}
