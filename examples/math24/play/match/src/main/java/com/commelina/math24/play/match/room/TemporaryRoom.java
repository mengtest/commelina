package com.commelina.math24.play.match.room;

import akka.actor.Props;
import com.commelina.akka.dispatching.proto.ActorBroadcast;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.matching_room.proto.CREATE_ROOM_REQUEST;
import com.commelina.math24.matching_room.proto.MATCH_ROOM_REQUEST_OPCODE;
import com.commelina.math24.play.match.AbstractMatchServiceActor;
import com.commelina.math24.play.match.proto.*;
import com.google.common.collect.Sets;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author panyao
 * @date 2017/11/10
 */
class TemporaryRoom extends AbstractMatchServiceActor {

    /**
     * 房间id
     */
    private final Long roomId;
    /**
     * 用户列表
     */
    private final List<Long> userIds;

    private final Set<Long> joinUserIds = Sets.newHashSet();
    private final Set<Long> offlineUserIds = Sets.newHashSet();

    private static final FiniteDuration DISSOLVE = Duration.create(2, TimeUnit.MINUTES);

    /**
     * 创建临时房间写xs后无错误返回，则表示操作成功
     */
    private static final FiniteDuration CREATE_REAL_ROOM_AFTER_CLOSE_TIME = Duration.create(10, TimeUnit.SECONDS);

    public TemporaryRoom(Long roomId, List<Long> userIds) {
        this.roomId = roomId;
        this.userIds = userIds;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 只传入 uid 则表示用户加入房间
                .match(JoinTemporaryRoom.class, j -> {
                    joinUserIds.add(j.getUserId());
                    if (joinUserIds.size() >= userIds.size()) {
                        createRoomInRoomServer();
                    }
                })
                .match(MemberOfflineEvent.class, offlineEvent -> {
                    offlineUserIds.add(offlineEvent.getLogoutUserId());
                    if (offlineUserIds.size() >= userIds.size()) {
                        dissolve();
                    }
                })
                .match(MemberOnlineEvent.class, onlineEvent -> offlineUserIds.remove(onlineEvent.getLoginUserId()))
                .match(TemporaryRoomPrepare.class, p -> {
                    // 加入临时房间广播
                    selectFrontend().tell(ActorBroadcast.newBuilder()
                                    .setOpcode(NOTIFY_OPCODE.JOIN_MATCH_TEMPORARY_ROOM_VALUE)
                                    .addAllUserIds(userIds)
                                    .setMessage(JOIN_MATCH_TEMPORARY_ROOM_BRD.newBuilder().setRoomId(roomId).build().toByteString())
                                    .build()
                            , getSelf());

                    // FIXME: 2017/11/13 超时设定
                    // 最后的关闭操作
                    getScheduler().scheduleOnce(DISSOLVE, this::dissolve, getDispatcher());
                })
                .build();
    }

    public static Props props(Long roomId, List<Long> userIds) {
        return Props.create(TemporaryRoom.class, roomId, userIds);
    }

    private void dissolve() {
        selectRoomManger().tell(TemporaryRoomDissolve.newBuilder()
                .setRoomId(roomId)
                .addAllUserIds(userIds)
                .build(), getSelf());
    }

    private void createRoomInRoomServer() {
        selectFrontend().tell(ApiRequestForward.newBuilder()
                .setForward(DOMAIN.GAME_ROOM_VALUE)
                .setOpcode(MATCH_ROOM_REQUEST_OPCODE.CREATE_ROOM_VALUE)
                .setVersion("1.0.0")
                .addArgs(CREATE_ROOM_REQUEST.newBuilder().addAllUserIds(userIds).build().toByteString())
                .build(), getSelf());
        getScheduler().scheduleOnce(CREATE_REAL_ROOM_AFTER_CLOSE_TIME, () -> getContext().stop(getSelf()), getDispatcher());
    }

}
