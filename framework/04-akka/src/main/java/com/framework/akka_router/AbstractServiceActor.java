package com.framework.akka_router;

import akka.actor.AbstractActor;
import com.framework.message.ApiRequest;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor implements Dispatch {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    public final void response(MessageBus message) {
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

}
