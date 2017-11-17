package com.commelina.math24.play.match.room;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.commelina.math24.play.match.AbstractMatchServiceActor;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author panyao
 * @date 2017/11/10
 */
public class RoomManger extends AbstractMatchServiceActor {

    /**
     * 零时房间列表
     */
    private final Map<Long, ActorRef> roomList = Maps.newHashMap();

    private long currentLastRoomId = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(List.class, this::createRoom)
                .build();
    }

    private void createRoom(List<Long> userIds) {
        final ActorRef temporary = getContext().getSystem().actorOf(TemporaryRoom.props(currentLastRoomId, userIds));

        roomList.put(currentLastRoomId++, temporary);

//        ApiRequestForward.Builder builder = ApiRequestForward.newBuilder()
//                .setForward(DOMAIN.GAME_ROOM_VALUE)
//                .setOpcode(MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE)
//                .setVersion("1.0.0");
//
//        for (Long userId : userIds) {
//            builder.addArgs(ByteString.copyFromUtf8(userId.toString()));
//        }

//        temporary.tell(CreateRoom.getDefaultInstance(), getSelf());
    }

    public static Props props() {
        return Props.create(RoomManger.class);
    }

}
