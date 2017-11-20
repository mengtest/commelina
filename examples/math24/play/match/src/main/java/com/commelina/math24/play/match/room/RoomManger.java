package com.commelina.math24.play.match.room;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.math24.play.match.AbstractMatchServiceActor;
import com.commelina.math24.play.match.proto.JoinTemporaryRoom;
import com.commelina.math24.play.match.proto.TemporaryRoomDissolve;
import com.commelina.math24.play.match.proto.TemporaryRoomPrepare;
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
                    ActorRef temporaryRoom = selectRoom(offlineEvent.getLogoutUserId());
                    if (temporaryRoom != null) {
                        temporaryRoom.forward(offlineEvent, getContext());
                    }
                })
                .match(MemberOnlineEvent.class, onlineEvent -> {
                    ActorRef temporaryRoom = selectRoom(onlineEvent.getLoginUserId());
                    if (temporaryRoom != null) {
                        temporaryRoom.forward(onlineEvent, getContext());
                    }
                })
                .match(JoinTemporaryRoom.class, j -> {
                    Long roomId = userRoomId.get(j.getUserId());
                    if (roomId == null || roomId == 0) {
                        getLogger().info("User : {} not found roomId.", j.getUserId());
                        return;
                    }

                    ActorRef temporaryRoom = roomList.get(roomId);
                    if (temporaryRoom == null) {
                        getLogger().info("User : {},roomId {} not found.", j.getUserId(), roomId);
                        return;
                    }

                    temporaryRoom.forward(j, getContext());
                })
                .match(List.class, this::createRoom)
                .match(TemporaryRoomDissolve.class, d -> {
                    roomList.remove(d.getRoomId());
                    d.getUserIdsList().forEach(userRoomId::remove);
                })
                .build();
    }

    private void createRoom(List<Long> userIds) {
        final ActorRef temporary = getContext().getSystem().actorOf(TemporaryRoom.props(currentLastRoomId, userIds));

        roomList.put(currentLastRoomId++, temporary);

        temporary.tell(TemporaryRoomPrepare.getDefaultInstance(), getSelf());
    }

    private ActorRef selectRoom(long userId) {
        Long roomId = userRoomId.get(userId);
        if (roomId == null || roomId <= 0) {
            return null;
        }
        return roomList.get(roomId);
    }

    public static Props props() {
        return Props.create(RoomManger.class);
    }

}
