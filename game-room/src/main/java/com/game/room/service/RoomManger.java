package com.game.room.service;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.framework.message.ApiRouterRequest;
import com.game.room.entity.PlayerEntity;
import com.game.room.portal.RoomClientClientRouter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.message.matching_room.proto.MethodDef;

import java.util.List;
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
                .build();
    }

    private void onServerRequest(ApiRouterRequest apiRouterRequest) {
        switch (apiRouterRequest.getApiOpcode().getNumber()) {
            case MethodDef.MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:
                onServerRequestCreateRoom(apiRouterRequest);
                break;
            default:
                this.unhandled(apiRouterRequest);
        }
    }

    private void onClientRequest(RoomClientRouterEntity roomClientRouterEntity) {
        ActorRef roomContext = roomIdToRoomContextActor.get(roomClientRouterEntity.getRoomId());
        if (roomContext == null) {
            getSender().tell(RoomClientClientRouter.NotFoundMessage(roomClientRouterEntity.getApiRequestWithActor().getApiOpcode()), getSelf());
            return;
        }
        roomContext.forward(roomClientRouterEntity, getContext());
    }

    private void onServerRequestCreateRoom(ApiRouterRequest apiRouterRequest) {

        if (roomClientRouterEntity.getApiRequestWithActor().isServer()) {
            switch (roomClientRouterEntity.getApiRequestWithActor().getApiOpcode().getNumber()) {
                case MethodDef.MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:
                    onServerRequestCreateRoom(roomClientRouterEntity.getApiRequestWithActor());
                    break;
                default:
                    this.unhandled(roomClientRouterEntity);
            }
        }

        long[] userIds = new long[apiRequestWithActor.getArgs().length];
        for (int i = 0; i < apiRequestWithActor.getArgs().length; i++) {
            userIds[i] = apiRequestWithActor.getArgs()[i].getAsLong();
        }

        // 加载用户信息
        List<PlayerEntity> playerEntities = Lists.newArrayListWithExpectedSize(apiRequestWithActor.getArgs().length);

        final long newRoomId = ++roomId;
        ActorRef roomContext = getContext().actorOf(RoomContext.props(newRoomId, playerEntities), "roomContext");
        roomIdToRoomContextActor.put(newRoomId, roomContext);
    }

    public static Props props() {
        return null;
    }

}
