package com.game.room.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.game.room.portal.RoomRouter;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomManger extends AbstractActor {

    final Map<Long, ActorRef> roomIdToRoomContextActor = Maps.newLinkedHashMap();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RoomRouterEntity.class, this::onRequest)
                .build();
    }

    private void onRequest(RoomRouterEntity roomRouterEntity) {
        ActorRef roomContext = roomIdToRoomContextActor.get(roomRouterEntity.getRoomId());
        if (roomContext == null) {
            getSender().tell(RoomRouter.NotFoundMessage(roomRouterEntity.getApiRequestWithActor().getApiOpcode()), getSelf());
            return;
        }
        roomContext.forward(roomRouterEntity, getContext());
    }

    public static Props props() {
        return null;
    }

}
