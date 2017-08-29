package com.instruction.gateway.portal;

import akka.actor.Props;
import com.instruction.gateway.proto.GATEWAY_APIS;
import com.nexus.maven.netty.socket.ActorWithApiController;
import com.nexus.maven.netty.socket.ActorWithApiHandler;
import com.nexus.maven.netty.socket.ChannelOutputHandler;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController(apiName = "" + GATEWAY_APIS.GAME_ROOM_REDIRECT_v1_0_0_VALUE)
public class GameRoomActor implements ActorWithApiHandler {

    @Value("akka.remote.actor.roomPath:xxxxxxxxxxxxxxxxxxxxxxxxx")
    private String remotePath;

    @Override
    public Props getProps(ChannelOutputHandler outputHandler) {
        return null;
    }
    // DOMAIN_CONSTANTS.GAME_ROOM_VALUE

}
