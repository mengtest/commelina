package com.commelina.math24.play.match.mode;

import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.commelina.akka.dispatching.proto.ActorBroadcast;
import com.commelina.math24.play.match.AbstractMatchServiceActor;
import com.commelina.math24.play.match.proto.MATCH_STATUS_BRD;
import com.commelina.math24.play.match.proto.NOTIFY_OPCODE;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author panyao
 * @date 2017/11/10
 */
class GlobalMatchStatus extends AbstractMatchServiceActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(List.class, this::notifyMatchStatus)
                .build();
    }

    private void notifyMatchStatus(List<Long> userIds) {
        log.info("Broadcast match status people: " + userIds.size());

        selectFrontend().tell(
                ActorBroadcast.newBuilder()
                        .setOpcode(NOTIFY_OPCODE.MATCH_STATUS_VALUE)
                        .addAllUserIds(userIds)
                        .setMessage(MATCH_STATUS_BRD.newBuilder().setMatchPeople(userIds.size()).build().toByteString())
                        .build(),
                getSelf());

        // xs 后关闭此 actor
        getScheduler().scheduleOnce(
                Duration.create(5, TimeUnit.SECONDS),
                () -> getContext().stop(getSelf()),
                getDispatcher()
        );
    }

    static Props props() {
        return Props.create(GlobalMatchStatus.class);
    }

}
