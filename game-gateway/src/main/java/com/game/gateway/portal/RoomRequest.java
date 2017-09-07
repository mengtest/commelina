package com.game.gateway.portal;

import akka.actor.Props;
import com.framework.message.ApiLoginRequest;
import com.framework.message.ApiRequest;
import com.framework.message.*;
import com.framework.niosocket.*;
import com.game.gateway.AkkaRemoteActorEntity;
import com.game.gateway.MessageProvider;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorRequestController(apiPathCode = com.game.gateway.proto.GATEWAY_APIS.GAME_ROOM_V1_0_0_VALUE)
public class RoomRequest implements ActorRequest {

    @Resource
    private AkkaRemoteActorEntity akkaRemoteActorEntity;

    @Override
    public Props getProps(ChannelOutputHandler outputHandler) {
        return ActorRequestRemoteProxyHandler.props(
                RoomRemoteProxyRouterActorRequestRequestRemoteProxy.class,
                DOMAIN.GAME_ROOM_VALUE,
                akkaRemoteActorEntity.getRoomRequestPath(),
                outputHandler
        );
    }

    private static class RoomRemoteProxyRouterActorRequestRequestRemoteProxy extends ActorRequestRemoteProxyHandler {

        public RoomRemoteProxyRouterActorRequestRequestRemoteProxy(int domain, String remotePath, ChannelOutputHandler context) {
            super(domain, remotePath, context);
        }

        @Override
        public void onRequest(ApiRequest request) {
            long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
            if (userId <= 0) {
                // 不登录,直接告诉客户端错误
                ResponseMessage message = ResponseMessage.newMessage(request.getApiOpcode(),
                        MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_UNAUTHORIZED)));

                ResponseMessageDomain messageDomain = ResponseMessageDomain.newResponseMessageDomain(DOMAIN.MATCHING_VALUE, message);

                // 回复消息到 gateway domain
                getSelf().tell(messageDomain, getSelf());
                return;
            }

            RequestArg roomId = request.getArg(0);
            if (roomId == null || roomId.getAsLong() <= 0) {
                // 第一个参数必须是 room Id
                ResponseMessage message = ResponseMessage.newMessage(request.getApiOpcode(),
                        MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_IMPORT_ROOM_ID))
                );

                ResponseMessageDomain messageDomain = ResponseMessageDomain.newResponseMessageDomain(DOMAIN.MATCHING_VALUE, message);

                // 回复消息到 gateway domain
                getSelf().tell(messageDomain, getSelf());
                return;
            }

            getSelf().tell(ApiLoginRequest.newClientApiRequestWithActor(userId, request.getApiOpcode(), request.getVersion(), request.getArgs()), getSelf());
        }

    }
}
