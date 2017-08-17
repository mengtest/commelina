package com.game.matching.service;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.game.matching.MessageProvider;
import com.game.matching.OpCodeConstants;
import com.nexus.maven.akka.AkkaBroadcast;

/**
 * Created by @panyao on 2017/8/14.
 * <p>
 * 同步匹配状态dao到客户端
 */
public class MatchingStatus extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

//    private final ActorSelection matchingGroup = getContext().system().actorSelection(getContext().parent().path().parent());

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(NOTIFY_MATCH_STATUS.class, ms -> {
                    AkkaBroadcast broadcast = AkkaBroadcast.newBroadcast(ms.userIds,
                            MessageProvider.newMessageForKV(OpCodeConstants.NOTIFY_MATCH_SUCCESS, "matchUserCount", ms.userIds.length));
                    log.info("Broadcast match status people: " + ms.userIds.length);
                    // 把消息发回到主 actor 由，主 actor 发送广播消息到 gate way
                    getSender().tell(broadcast, getSelf());
//                    getSender().tell(broadcast, getSelf());
                })
                .matchAny(o -> log.info("MatchingStatus received unknown message "))
                .build();
    }

    static final class NOTIFY_MATCH_STATUS {
        long[] userIds;

        NOTIFY_MATCH_STATUS(long[] userIds) {
            this.userIds = userIds;
        }
    }

    static Props props() {
        return Props.create(MatchingStatus.class);
    }

}