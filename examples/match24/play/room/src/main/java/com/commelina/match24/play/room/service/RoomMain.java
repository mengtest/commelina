package com.commelina.match24.play.room.service;

import akka.actor.Props;
import com.commelina.akka.dispatching.cluster.nodes.AbstractServiceActor;
import com.commelina.akka.dispatching.cluster.nodes.ClusterChildNodeSystem;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.core.BusinessMessage;
import com.commelina.match24.play.room.bm.NotifyJoinRoom;
import com.commelina.match24.play.room.entity.PlayerEntity;
import com.commelina.match24.play.room.event.PlayerStatusEvent;
import com.commelina.match24.play.room.proto.OPCODE;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author @panyao
 * @date 2017/8/17
 */
public class RoomMain extends AbstractServiceActor {

    private final long roomId;

    private final BiMap<Long, PlayerEntity> players = HashBiMap.create(8);

    /**
     * 10 分钟之后结束游戏
     */
    private final FiniteDuration lazyCheckOver = Duration.create(10, TimeUnit.MINUTES);

    public RoomMain(long roomId, List<PlayerEntity> playerEntities) {
        this.roomId = roomId;
        playerEntities.forEach(v -> players.put(v.getUserId(), v));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getContext().getSystem().scheduler().scheduleOnce(lazyCheckOver, () -> {
            // TODO: 2017/10/11 检查游戏结束
        }, getContext().getSystem().dispatcher());
        // 给客户端发送广播
        sendJoinRoomBroadcast();
    }

    private void sendJoinRoomBroadcast() {
        NotifyJoinRoom room = new NotifyJoinRoom();
        room.setRoomId(roomId);
        ClusterChildNodeSystem.INSTANCE.broadcast(
                OPCODE.JOIN_ROOM_VALUE,
                players.keySet(),
                BusinessMessage.success(room)
        );
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PlayerStatusEvent.class, statusEvent -> {
                    PlayerEntity playerEntity = players.get(statusEvent.getUserId());
                    if (playerEntity != null) {
                        playerEntity.setPlayerStatus(statusEvent.getStatus());
                    }
                })
                .match(ApiRequest.class, r -> {
                    // TODO: 2017/11/2 处理具体业务
                })
                .build();
    }

    public static Props props(long roomId, List<PlayerEntity> playerEntities) {
        return Props.create(RoomMain.class, roomId, playerEntities);
    }

}
