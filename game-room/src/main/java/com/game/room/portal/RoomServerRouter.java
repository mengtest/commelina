package com.game.room.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.framework.akka.AbstractServerRouterActor;
import com.framework.akka.ApiRequestWithActor;
import com.framework.message.ServerRouterMessage;
import com.game.room.entity.PlayerEntity;

import java.util.List;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomServerRouter extends AbstractServerRouterActor {

    private final ActorRef roomManger;

    public RoomServerRouter(ActorRef roomManger) {
        this.roomManger = roomManger;
    }

    @Override
    public void onRequest(ApiRequestWithActor request) {
        // 服务端直接的请求
        roomManger.forward(ServerRouterMessage.newServerRouterMessage(request), getContext());
    }

    public static Props props(ActorRef roomManger) {
        return Props.create(RoomServerRouter.class, roomManger);
    }

    public static class CreateRoomEntity {
        private List<PlayerEntity> players;

        public List<PlayerEntity> getPlayers() {
            return players;
        }

        public void setPlayers(List<PlayerEntity> players) {
            this.players = players;
        }
    }

}
