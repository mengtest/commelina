package com.commelina.math24.play.room.context;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.commelina.akka.cluster.nodes.AbstractServiceActor;
import com.commelina.akka.cluster.nodes.ClusterChildNodeSystem;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.core.BusinessMessage;
import com.commelina.math24.play.room.entity.PlayerEntity;
import com.commelina.math24.play.room.entity.PlayerStatus;
import com.commelina.math24.play.room.message.NotifyJoinRoom;
import com.commelina.math24.play.room.proto.Prepare;
import com.commelina.math24.play.room.proto.Prepared;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 房间
 *
 * @author @panyao
 * @date 2017/8/17
 */
public class RoomContext extends AbstractServiceActor {

    private final long roomId;
    private final BiMap<Long, PlayerEntity> players = HashBiMap.create(8);

    /**
     * 棋盘 actorRef
     */
    private ActorRef checkerboard;
    /**
     * 棋盘准备状态
     */
    private boolean checkerboardPrepared;
    /**
     * 处于哪个阶段
     */
    private MemberOfPrepareBehavior ofBehavior;

    public RoomContext(long roomId, List<PlayerEntity> playerEntities) {
        this.roomId = roomId;
        playerEntities.forEach(v -> players.put(v.getUserId(), v));
    }

    @Override
    public void preStart() throws Exception {
        ofBehavior = MemberOfPrepareBehavior.NONE;
        ActorSystem system = getContext().getSystem();

        // 设置检查游戏结束的任务
        // 10 分钟之后结束游戏
        system.scheduler().scheduleOnce(Duration.create(11, TimeUnit.MINUTES), this::checkOver, system.dispatcher());

        // 创建棋盘
        checkerboard = system.actorOf(Checkerboard.props(100, 100));

        // 发送准备棋盘的通知
        checkerboard.tell(Prepare.getDefaultInstance(), getSelf());

        // 给客户端发送加入房间广播
        sendJoinRoomBroadcast();
    }

    private void sendJoinRoomBroadcast() {
        FiniteDuration finiteDuration = Duration.create(10, TimeUnit.SECONDS);

        NotifyJoinRoom room = new NotifyJoinRoom();
        room.setRoomId(roomId);
        room.setOverMicrosecond(System.currentTimeMillis() + finiteDuration.toMillis());

        ClusterChildNodeSystem.INSTANCE.broadcast(
                0,
//                OPCODE.JOIN_ROOM_VALUE,
                players.keySet(),
                BusinessMessage.success(room)
        );
        ActorSystem system = getContext().getSystem();
        // 十秒之后
        players.keySet().forEach(u -> system.scheduler().scheduleOnce(finiteDuration, () -> {

        }, system.dispatcher()));
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, r -> {
                    //

                })
                .match(MemberOfflineEvent.class, offlineEvent -> {
                    PlayerEntity playerEntity = players.get(offlineEvent.getLogoutUserId());
                    if (playerEntity != null) {
                        playerEntity.setPlayerStatus(PlayerStatus.Offline);
                    }
                })
                .match(MemberOnlineEvent.class, onlineEvent -> {
                    PlayerEntity playerEntity = players.get(onlineEvent.getLoginUserId());
                    if (playerEntity != null) {
                        playerEntity.setPlayerStatus(PlayerStatus.Online);
                    }
                })
                // 标记棋盘为准备完成状态
                .match(Prepared.class, p -> checkerboardPrepared = true)
                .build();
    }

    private boolean switchBehavior(ApiRequest r) {

        return true;
    }

    private void checkOver() {

    }

    public static Props props(long roomId, List<PlayerEntity> playerEntities) {
        return Props.create(RoomContext.class, roomId, playerEntities);
    }

}
