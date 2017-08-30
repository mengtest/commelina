package com.module.foundation.game.gateway.portal;

import akka.actor.Props;
import com.module.foundation.game.gateway.proto.GATEWAY_APIS;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiHandler;
import com.nexus.maven.netty.socket.ChannelOutputHandler;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiPathCode = GATEWAY_APIS.GAME_ROOM_V1_0_0_VALUE)
public class GameRoomRouterActor implements ActorWithApiHandler {

    @Value("akka.remote.actor.roomPath:xxxxxxxxxxxxxxxxxxxxxxxxx")
    private String remotePath;

    @Override
    public Props getProps(ChannelOutputHandler outputHandler) {
        return null;
    }
    // DOMAIN_CONSTANTS.GAME_ROOM_VALUE

}
