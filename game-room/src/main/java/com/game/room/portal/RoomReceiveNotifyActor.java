package com.game.room.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.framework.akka.AbstractForwardRemoteReceiveActor;
import com.framework.akka.MemberOfflineEvent;
import com.framework.akka.MemberOnlineEvent;
import com.framework.message.ApiRequestForward;
import com.game.room.entity.PlayerEntity;
import com.google.common.collect.Lists;
import com.message.matching_room.proto.MATCHING_ROOM_METHODS;

import java.util.List;

/**
 * Created by @panyao on 2017/9/7.
 */
public class RoomReceiveNotifyActor extends AbstractForwardRemoteReceiveActor {

    private final ActorRef roomManger;

    public RoomReceiveNotifyActor(ActorRef roomManger) {
        this.roomManger = roomManger;
    }

    @Override
    public void onEvent(MemberOnlineEvent onlineEvent) {

    }

    @Override
    public void onEvent(MemberOfflineEvent offlineEvent) {

    }

    @Override
    public void onForwardEvent(ApiRequestForward forward) {
        switch (forward.getApiOpcode().getNumber()) {
            case MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:
                this.createRoom(forward);
                break;
        }
    }

    private void createRoom(ApiRequestForward request) {
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
        return Props.create(RoomReceiveNotifyActor.class, roomManger);
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
