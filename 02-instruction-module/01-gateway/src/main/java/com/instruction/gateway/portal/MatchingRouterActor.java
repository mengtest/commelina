package com.instruction.gateway.portal;

import akka.actor.Props;
import com.instruction.gateway.proto.DOMAIN_CONSTANTS;
import com.instruction.gateway.proto.GATEWAY_APIS;
import com.nexus.maven.core.message.*;
import com.nexus.maven.netty.socket.*;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "" + GATEWAY_APIS.MATCHING_REDIRECT_V1_0_0_VALUE)
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
                // FIXME: 2017/8/25 必须登陆
            }

            getSelf().tell(ApiRequestWithActor.newApiRequestWithActor(userId, request.getApiMethod(), request.getVersion(), request.getArgs()), getSelf());
        }

    }

}
