package com.framework.akka_router.cluster.node;

import akka.actor.AbstractActor;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor {

    public final void response(MessageBus message) {
        // 回复到 on request / on forward 的发送者那里
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

}