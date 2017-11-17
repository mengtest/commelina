package com.commelina.math24.play.match.room;

import akka.actor.Props;
import com.commelina.math24.play.match.AbstractMatchServiceActor;
import com.commelina.math24.play.match.proto.PrepareTemporaryRoom;

import java.util.List;

/**
 * @author panyao
 * @date 2017/11/10
 */
public class TemporaryRoom extends AbstractMatchServiceActor {

    /**
     * 房间id
     */
    private final Long roomId;
    /**
     * 用户列表
     */
    private final List<Long> userIds;

    public TemporaryRoom(Long roomId, List<Long> userIds) {
        this.roomId = roomId;
        this.userIds = userIds;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PrepareTemporaryRoom.class, p ->{
                    // FIXME: 2017/11/13 超时设定
                })
                .build();
    }

    public static Props props(Long roomId, List<Long> userIds) {
        return Props.create(TemporaryRoom.class, roomId, userIds);
    }

}
