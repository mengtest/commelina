package com.framework.akka_router.cluster.node;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.ResponseEntity;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    protected final void response(MessageBus message) {
        // 使用 此方法，必须是有 actor.forward 从定向过来的 ask
        // 回复到 on request 的发送者那里
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

    protected final void forwardResponse(ResponseEntity entity) {
        // 使用 此方法，必须是有 actor.forward 从定向过来的 ask
        // 回复到 on forward 的发送者那里
        getSender().tell(entity, getSelf());
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }

}