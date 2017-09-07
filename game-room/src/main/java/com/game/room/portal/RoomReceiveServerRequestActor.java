package com.game.room.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.framework.akka.AbstractReceiveServerRequestActor;
import com.framework.message.ApiRequest;
import com.game.room.entity.PlayerEntity;
import com.google.common.collect.Lists;
import com.message.matching_room.proto.MATCHING_ROOM_METHODS;

import java.util.List;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomReceiveServerRequestActor extends AbstractReceiveServerRequestActor {

    private final ActorRef roomManger;

    public RoomReceiveServerRequestActor(ActorRef roomManger) {
        this.roomManger = roomManger;
    }

    @Override
    public void onRequest(ApiRequest request) {
        switch (request.getApiOpcode().getNumber()) {
            case MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:
                this.createRoom(request);
                break;
        }

        // 服务端直接的请求
//        roomManger.forward(ServerRouterMessage.newServerRouterMessage(request), getContext());
    }

    private void createRoom(ApiRequest request) {
        long[] userIds = new long[request.getArgs().length];
        for (int i = 0; i < request.getArgs().length; i++) {
            userIds[i] = request.getArgs()[i].getAsLong();
        }

        // 加载用户信息
        final List<PlayerEntity> playerEntities = Lists.newArrayListWithExpectedSize(request.getArgs().length);
        final CreateRoomEntity createRoomEntity = new CreateRoomEntity();
        createRoomEntity.setPlayers(playerEntities);
        roomManger.tell(createRoomEntity, getSelf());
    }

    public static Props props(ActorRef roomManger) {
        return Props.create(RoomReceiveServerRequestActor.class, roomManger);
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
