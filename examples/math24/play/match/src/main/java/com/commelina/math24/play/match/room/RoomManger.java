package com.commelina.math24.play.match.room;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.math24.play.match.AbstractMatchServiceActor;
import com.commelina.math24.play.match.proto.PrepareTemporaryRoom;
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
    private final Map<Long, ActorRef> roomList = Maps.newTreeMap();

    /**
     * userId -> roomId
     */
    private final Map<Long, Long> userRoomId = Maps.newTreeMap();

    private long currentLastRoomId = 0;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MemberOfflineEvent.class, offlineEvent -> {
                    Long roomId = userRoomId.get(offlineEvent.getLogoutUserId());
                    if (roomId != null && roomId > 0) {
                        ActorRef temporaryRoom = roomList.get(roomId);
                        if (temporaryRoom != null) {
                            temporaryRoom.forward(offlineEvent, getContext());
                        }
                    }
                })
                .match(MemberOnlineEvent.class, onlineEvent -> {
                    Long roomId = userRoomId.get(onlineEvent.getLoginUserId());
                    if (roomId != null && roomId > 0) {
                        ActorRef temporaryRoom = roomList.get(roomId);
                        if (temporaryRoom != null) {
                            temporaryRoom.forward(onlineEvent, getContext());
                        }
                    }
                })
                .match(List.class, this::createRoom)
                .build();
    }

    private void createRoom(List<Long> userIds) {
        final ActorRef temporary = getContext().getSystem().actorOf(TemporaryRoom.props(currentLastRoomId, userIds));

        roomList.put(currentLastRoomId++, temporary);

        temporary.tell(PrepareTemporaryRoom.getDefaultInstance(), getSelf());
    }

    public static Props props() {
        return Props.create(RoomManger.class);
    }

}
