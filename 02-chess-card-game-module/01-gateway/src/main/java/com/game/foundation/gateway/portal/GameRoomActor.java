package com.game.foundation.gateway.portal;

import com.game.gateway.proto.ConstantsDef;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiHandler;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "/game/room/redirect/v1.0.0")
public class GameRoomActor implements ActorWithApiHandler {

    @Value("akka.remote.actor.roomPath:xxxxxxxxxxxxxxxxxxxxxxxxx")
    private String remotePath;

    @Override
    public int getDomain() {
        return ConstantsDef.DOMAIN_CONSTANTS.GAME_ROOM_VALUE;
    }

    @Override
    public RequestEvent getRouterEvent() {
        return null;
    }
}
