package com.game.foundation.gateway.portal;

import akka.actor.Props;
import com.game.foundation.gateway.constants.DomainConstants;
import com.nexus.maven.netty.socket.ActorWithApiRequest;
import com.nexus.maven.netty.socket.ActorAkkaContext;
import com.nexus.maven.netty.socket.ActorResponseContext;
import com.nexus.maven.netty.socket.ActorWithApiController;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController
public class GameRoomActorWithApiRequest implements ActorWithApiRequest {

    @Value("akka.remote.actor.roomPath:xxxxxxxxxxxxxxxxxxxxxxxxx")
    private String remotePath;

    @Override
    public String getApiName() {
        return "/game/room/redirect/v1.0.0";
    }

    @Override
    public Props getProps(ActorResponseContext context) {
        return ActorAkkaContext.RemoteProxyActor.props(DomainConstants.DOMAIN_GAME_ROOM.ordinal(), remotePath, context);
    }

}
