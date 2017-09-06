package com.game.room.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.framework.akka.ApiRequestWithActor;
import com.framework.message.ServerRouterMessage;
import com.game.room.portal.RoomRouter;
import com.google.common.collect.Maps;
import com.message.matching_room.proto.MethodDef;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomManger extends AbstractActor {

    private final Map<Long, ActorRef> roomIdToRoomContextActor = Maps.newLinkedHashMap();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(RoomRouterEntity.class, this::onClientRequest)
                .match(ServerRouterMessage.class, this::onServerRequest)
                .build();
    }

    private void onServerRequest(ServerRouterMessage serverRouterMessage) {
        ApiRequestWithActor apiRequestWithActor = serverRouterMessage.getApiRequestWithActor();
        switch (apiRequestWithActor.getApiOpcode().getNumber()) {
            case MethodDef.MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:

                break;
            default:
                this.unhandled(serverRouterMessage);
        }
    }

    private void onClientRequest(RoomRouterEntity roomRouterEntity) {
        ActorRef roomContext = roomIdToRoomContextActor.get(roomRouterEntity.getRoomId());
        if (roomContext == null) {
            getSender().tell(RoomRouter.NotFoundMessage(roomRouterEntity.getApiRequestWithActor().getApiOpcode()), getSelf());
            return;
        }
        if (roomRouterEntity.getApiRequestWithActor().isServer()) {

        } else {
            roomContext.forward(roomRouterEntity, getContext());
        }
    }

    public static Props props() {
        return null;
    }

}
