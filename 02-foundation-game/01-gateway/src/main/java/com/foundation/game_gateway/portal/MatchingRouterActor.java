package com.foundation.game_gateway.portal;

import akka.actor.Props;
import com.foundation.game_gateway.MessageProvider;
import com.foundation.game_gateway.proto.DOMAIN_CONSTANTS;
import com.foundation.game_gateway.proto.ERROR_CODE_CONSTANTS;
import com.foundation.game_gateway.proto.GATEWAY_APIS;
import com.framework.core_message.ApiRequestWithActor;
import com.framework.core_message.ApiRouterRequest;
import com.framework.core_message.BusinessMessage;
import com.framework.core_message.ResponseMessage;
import com.framework.netty_socket.*;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiPathCode = GATEWAY_APIS.MATCHING_V1_0_0_VALUE)
public class MatchingRouterActor implements ActorWithApiHandler {

    @Value("akka.remote.actor.matchingPath:akka.tcp://MatchingWorkerSystem@127.0.0.1:2551/user/matchingRouter")
    private String remotePath;

    @Override
    public Props getProps(ChannelOutputHandler outputHandler) {
        return MatchingRemoteProxyRouterActor.props(MatchingRemoteProxyRouterActor.class, DOMAIN_CONSTANTS.MATCHING_VALUE, remotePath, outputHandler);
    }

    private static class MatchingRemoteProxyRouterActor extends ActorWithRemoteProxyRouter {

        public MatchingRemoteProxyRouterActor(int domain, String remotePath, ChannelOutputHandler context) {
            super(domain, remotePath, context);
        }

        @Override
        public boolean onRequest(ApiRouterRequest request) {
            long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
            if (userId <= 0) {
                // 不登录直接告诉客户端错误
                getSelf().tell(ResponseMessage.newMessage(request.getApiOpcode(),
                        MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE_CONSTANTS.MATCHING_API_UNAUTHORIZED))
                ), getSelf());
                return true;
            }
            getSelf().tell(ApiRequestWithActor.newApiRequestWithActor(userId, request.getApiOpcode(), request.getVersion(), request.getArgs()), getSelf());
            return true;
        }

    }

}
