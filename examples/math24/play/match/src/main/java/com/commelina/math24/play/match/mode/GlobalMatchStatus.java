package com.commelina.math24.play.match.mode;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.commelina.akka.cluster.nodes.ClusterChildNodeSystem;
import com.commelina.core.DefaultMessageProvider;
import com.commelina.math24.play.match.proto.OPCODE;

import java.util.List;

/**
 * @author panyao
 * @date 2017/11/10
 */
public class GlobalMatchStatus extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(List.class, this::notifyMatchStatus)
                .build();
    }

    private void notifyMatchStatus(List<Long> userIds) {
        log.info("Broadcast match status people: " + userIds.size());

        // 把消息发回到主 actor 由，主 actor 发送广播消息到 gate way
        ClusterChildNodeSystem.INSTANCE.broadcast(
                OPCODE.NOTIFY_MATCH_SUCCESS_VALUE,
                userIds,
                DefaultMessageProvider.produceMessageForKV("matchUserCount", userIds.size()));

        // 关闭此 actor
        getContext().stop(getSelf());
    }

    public static Props props() {
        return Props.create(GlobalMatchStatus.class);
    }

}
