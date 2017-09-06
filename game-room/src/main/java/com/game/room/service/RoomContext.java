package com.game.room.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.game.room.entity.PlayerEntity;

import java.util.List;

/**
 * Created by @panyao on 2017/8/17.
 */
class RoomContext extends AbstractActor {

    private final long roomId;
    private final List<PlayerEntity> playerEntities;

    public RoomContext(long roomId, List<PlayerEntity> playerEntities) {
        this.roomId = roomId;
        this.playerEntities = playerEntities;
    }

    @Override
    public Receive createReceive() {
        return null;
    }

    static Props props(long roomId, List<PlayerEntity> playerEntities) {
        return Props.create(RoomContext.class, roomId, playerEntities);
    }

}
