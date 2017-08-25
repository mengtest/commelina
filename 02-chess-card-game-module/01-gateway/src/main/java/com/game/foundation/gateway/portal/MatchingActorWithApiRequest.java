package com.game.foundation.gateway.portal;

import akka.actor.Props;
import com.game.foundation.gateway.constants.DomainConstants;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorAkkaContext;
import com.nexus.maven.netty.socket.ActorResponseContext;
import com.nexus.maven.netty.socket.ActorWithApiRequest;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController
public class MatchingActorWithApiRequest implements ActorWithApiRequest {

    @Value("akka.remote.actor.matchingPath:akka.tcp://MatchingWorkerSystem@127.0.0.1:2551/user/matchingRouter")
    private String remotePath;

    @Override
    public String getApiName() {
        return "/matching/redirect/v1.0.0";
    }

    @Override
    public Props getProps(ActorResponseContext context) {
        return ActorAkkaContext.RemoteProxyActor.props(DomainConstants.DOMAIN_MATCHING.ordinal(), remotePath, context);
    }

}
