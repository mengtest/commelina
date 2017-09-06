package com.game.room.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.game.room.portal.RoomClientClientRouter;
import com.game.room.portal.RoomServerRouter;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomManger extends AbstractActor {

    private final Map<Long, ActorRef> roomIdToRoomContextActor = Maps.newLinkedHashMap();
    private long roomId = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RoomClientRouterEntity.class, this::onClientRequest)
                .match(RoomServerRouter.CreateRoomEntity.class, this::createRoom)
                .build();
    }

    private void onClientRequest(RoomClientRouterEntity roomClientRouterEntity) {
        ActorRef roomContext = roomIdToRoomContextActor.get(roomClientRouterEntity.getRoomId());
        if (roomContext == null) {
            getSender().tell(RoomClientClientRouter.NotFoundMessage(roomClientRouterEntity.getApiRequestWithActor().getApiOpcode()), getSelf());
            return;
        }
        roomContext.forward(roomClientRouterEntity, getContext());
    }

    private void createRoom(RoomServerRouter.CreateRoomEntity createRoomEntity) {
        final long newRoomId = ++roomId;
        ActorRef roomContext = getContext().actorOf(RoomContext.props(newRoomId, createRoomEntity.getPlayers()), "roomContext");
        roomIdToRoomContextActor.put(newRoomId, roomContext);
    }

    public static Props props() {
        return null;
    }

}