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
public class MatchingActor implements ActorWithApiHandler {

    @Value("akka.remote.actor.matchingPath:akka.tcp://MatchingWorkerSystem@127.0.0.1:2551/user/matchingRouter")
    private String remotePath;

    @Override
    public Props getProps(ChannelOutputHandler outputHandler) {
        return MatchingRemoteProxyRouterActor.props(DOMAIN_CONSTANTS.MATCHING_VALUE, remotePath, outputHandler);
    }

    private static class MatchingRemoteProxyRouterActor extends ActorWithRemoteProxyRouter {

        public MatchingRemoteProxyRouterActor(int domain, String remotePath, ChannelOutputHandler context) {
            super(domain, remotePath, context);
        }

        @Override
        protected void onRequest(ApiRequest request) {
            RequestArg pathArg = request.getArg(0);
            if (pathArg == null) {
                // FIXME: 2017/8/25 远程路由地址
            }

            long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
            if (userId <= 0) {
                // FIXME: 2017/8/25 必须登陆
            }

            getSelf().tell(ApiRequestWithLogin.newInstance(pathArg.getAsString(), request.getVersion(), userId, request.subArg(1)), getSelf());
        }
    }

}
