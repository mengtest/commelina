package com.game.room.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.game.room.entity.PlayerEntity;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomManger extends AbstractActor {

    // roomId -> roomContextActorRef
    private final Map<Long, ActorRef> roomIdToRoomContextActor = Maps.newLinkedHashMap();

    // userId -> roomId
    private final BiMap<Long, Long> userRoomIds = HashBiMap.create(6);
    private long roomId = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateRoomEntity.class, this::createRoom)
                .build();
    }

    private void createRoom(CreateRoomEntity createRoomEntity) {
        final long newRoomId = ++roomId;
        ActorRef roomContext = getContext().actorOf(RoomContext.props(newRoomId, createRoomEntity.getPlayers()), "roomContext");
        roomIdToRoomContextActor.put(newRoomId, roomContext);
    }

    public static Props props() {
        return Props.create(RoomManger.class);
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