package com.game.room.service;

import akka.actor.Props;
import com.framework.akka.router.cluster.nodes.AbstractServiceActor;
import com.game.room.entity.PlayerEntity;
import com.game.room.event.PlayerStatusEvent;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author @panyao
 * @date 2017/8/17
 */
 class RoomContext extends AbstractServiceActor {

    private final long roomId;

    private final BiMap<Long, PlayerEntity> players = HashBiMap.create(128);

    /**
     * 10 分钟之后结束游戏
     */
    private final FiniteDuration lazyCheckOver = Duration.create(10, TimeUnit.MINUTES);

    public RoomContext(long roomId, List<PlayerEntity> playerEntities) {
        this.roomId = roomId;
        playerEntities.forEach(v -> players.put(v.getUserId(), v));
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getContext().getSystem().scheduler().scheduleOnce(lazyCheckOver, () -> {
            // TODO: 2017/10/11 检查游戏结束
        }, getContext().getSystem().dispatcher());
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
        return Props.create(RoomContext.class, roomId, playerEntities);
    }

}
