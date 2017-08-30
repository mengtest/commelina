package com.foundation.game_gateway.service.portal;

import akka.actor.Props;
import com.foundation.game_gateway.proto.GATEWAY_APIS;
import com.framework.netty_socket.ActorWithApiController;
import com.framework.netty_socket.ActorWithApiHandler;
import com.framework.netty_socket.ChannelOutputHandler;
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
