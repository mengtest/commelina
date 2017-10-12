package com.framework.akka_router.cluster.node;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    public final void response(MessageBus message) {
        // 回复到 on request / on forward 的发送者那里
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }

}