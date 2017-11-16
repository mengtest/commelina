package com.commelina.akka.dispatching;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.commelina.akka.dispatching.proto.ActorResponse;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public abstract class AbstractServiceActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    protected final void response(ActorResponse message) {
        // 使用 此方法，必须是有 actor.forward 从定向过来的 ask
        // 回复到 on request 的发送者那里
        getSender().tell(message, getSelf());
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }

}