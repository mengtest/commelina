package com.game.room.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.game.room.entity.PlayerEntity;
import com.game.room.event.PlayerStatusEvent;
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
    private final BiMap<Long, ActorRef> roomIdToRoomContextActor = HashBiMap.create(128);

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    // userId -> roomId
    private final Map<Long, Long> usersToRoomId = Maps.newHashMap();
    private long roomId = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(CreateRoomEntity.class, this::createRoom)
                .match(PlayerStatusEvent.class, statusEvent -> {
                    Long roomId = usersToRoomId.get(statusEvent.getUserId());
                    if (roomId != null && roomId > 0) {
                        ActorRef roomContext = roomIdToRoomContextActor.get(roomId);
                        if (roomContext != null) {
                            roomContext.forward(statusEvent, getContext());
                        }
                    }
                })
                .build();
    }

    private void createRoom(CreateRoomEntity createRoomEntity) {
        final long newRoomId = roomId++;
        final ActorRef roomContext = getContext().actorOf(RoomContext.props(newRoomId, createRoomEntity.getPlayers()), "roomContext");
        roomIdToRoomContextActor.put(newRoomId, roomContext);
        createRoomEntity.getPlayers().forEach(v -> usersToRoomId.put(v.getUserId(), newRoomId));
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