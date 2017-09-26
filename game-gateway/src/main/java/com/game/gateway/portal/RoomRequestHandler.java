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
import io.netty.channel.ChannelHandlerContext;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/8/25.
 */
@NioSocketRouter(forward = 2)
@Deprecated
public class RoomRequestHandler {

    @Resource
    private AkkaRemoteActorEntity akkaRemoteActorEntity;

    public Props getProps(ChannelHandlerContext outputHandler) {
        return ActorRequestRemoteProxyWatching.props(
                RoomRemoteProxyRouterActorRequestRequestRemoteProxy.class,
                DOMAIN.GAME_ROOM_VALUE,
                akkaRemoteActorEntity.getRoomRequestPath(),
                outputHandler
        );
    }

    private static class RoomRemoteProxyRouterActorRequestRequestRemoteProxy extends ActorRequestRemoteProxyWatching {

        public RoomRemoteProxyRouterActorRequestRequestRemoteProxy(int domain, String remotePath, ChannelHandlerContext context) {
            super(domain, remotePath, context);
        }

        @Override
        public void onRequest(ApiRequest request) {
            long userId = ContextAdapter.getLoginUserId(context.channel().id());
            if (userId <= 0) {
                // 不登录,直接告诉客户端错误
                ResponseMessage message = ResponseMessage.newMessage(
                        MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_UNAUTHORIZED)));

                ResponseMessageDomain messageDomain = ResponseMessageDomain.newMessage(DOMAIN.MATCHING, message);

                // 回复消息到 gateway domain
                getSelf().tell(messageDomain, getSelf());
                return;
            }

            RequestArg roomId = request.getArg(0);
            if (roomId == null || roomId.getAsLong() <= 0) {
                // 第一个参数必须是 room Id
                ResponseMessage message = ResponseMessage.newMessage(
                        MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_IMPORT_ROOM_ID))
                );

                ResponseMessageDomain messageDomain = ResponseMessageDomain.newMessage(DOMAIN.MATCHING, message);

                // 回复消息到 gateway domain
                getSelf().tell(messageDomain, getSelf());
                return;
            }

            getSelf().tell(ApiRequestLogin.newRequest(userId, request.getOpcode(), request.getVersion(), request.getArgs()), getSelf());
        }

    }
}
