package com.game.foundation.gateway.portal;

import com.game.gateway.proto.ConstantsDef;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiRemoteHandler;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "/matching/redirect/v1.0.0")
public class MatchingActor implements ActorWithApiRemoteHandler {

    @Value("akka.remote.actor.matchingPath:akka.tcp://MatchingWorkerSystem@127.0.0.1:2551/user/matchingRouter")
    private String remotePath;

    @Override
    public int getDomain() {
        return ConstantsDef.DOMAIN_CONSTANTS.MATCHING_VALUE;
    }

    @Override
    public String getRemoteActorPath() {
        return remotePath;
    }

    @Override
    public RemoteRouterEvent getRouterEvent() {
        return null;
    }
}
