package com.framework.akka_cluster_router;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.framework.message.ApiRequestLogin;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor implements ServiceHandler {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequestLogin.class, this::onRequest)
                .build();
    }

    public final void response(MessageBus message) {
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

    @Override
    public Props getProps() {
        return Props.create(getClass());
    }

}
