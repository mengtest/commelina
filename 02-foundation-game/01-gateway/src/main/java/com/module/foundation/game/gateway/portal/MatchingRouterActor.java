package com.module.foundation.game.gateway.portal;

import akka.actor.Props;
import com.module.foundation.game.gateway.MessageProvider;
import com.module.foundation.game.gateway.proto.DOMAIN_CONSTANTS;
import com.module.foundation.game.gateway.proto.ERROR_CODE_CONSTANTS;
import com.module.foundation.game.gateway.proto.GATEWAY_APIS;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.ApiRequestWithActor;
import com.nexus.maven.core.message.BusinessMessage;
import com.nexus.maven.core.message.ResponseMessage;
import com.nexus.maven.netty.socket.*;
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
        public void onRequest(ApiRequest request) {
            long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
            if (userId <= 0) {
                getSelf().tell(ResponseMessage.newMessage(request.getApiOpcode(),
                        MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE_CONSTANTS.MATCHING_API_UNAUTHORIZED))
                ), getSelf());
                return;
            }
            getSelf().tell(ApiRequestWithActor.newApiRequestWithActor(userId, request.getApiOpcode(), request.getVersion(), request.getArgs()), getSelf());
        }

    }

}
