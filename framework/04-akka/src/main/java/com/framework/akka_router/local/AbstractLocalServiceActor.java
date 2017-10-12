package com.framework.akka_router.local;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.Dispatch;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequest;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractLocalServiceActor extends AbstractActor implements Dispatch {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    private Internal.EnumLite routerId;

    public AbstractLocalServiceActor(Internal.EnumLite routerId) {
        this.routerId = routerId;
    }

    @Override
    public void preStart() throws Exception {
        AkkaLocalWorkerSystem.INSTANCE.localRouterRegister(new RouterRegistrationEntity(routerId), getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    public final void response(MessageBus message) {
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }
}