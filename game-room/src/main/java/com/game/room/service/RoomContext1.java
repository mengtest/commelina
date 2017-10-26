package com.game.room.service;

import akka.actor.Props;
import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.AbstractServiceActor;
import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.ClusterChildNodeSystem;
import com.github.freedompy.commelina.core.BusinessMessage;
import com.game.room.BM.NotifyJoinRoom;
import com.game.room.entity.PlayerEntity;
import com.game.room.event.PlayerStatusEvent;
import com.game.room.proto.OPCODE;
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
class RoomContext1 extends AbstractServiceActor {

    private final long roomId;

    private final BiMap<Long, PlayerEntity> players = HashBiMap.create(8);

    /**
     * 10 分钟之后结束游戏
     */
    private final FiniteDuration lazyCheckOver = Duration.create(10, TimeUnit.MINUTES);

    public RoomContext1(long roomId, List<PlayerEntity> playerEntities) {
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
                .build();
    }

    static Props props(long roomId, List<PlayerEntity> playerEntities) {
        return Props.create(RoomContext1.class, roomId, playerEntities);
    }

}
