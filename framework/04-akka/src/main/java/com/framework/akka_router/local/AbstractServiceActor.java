package com.framework.akka_router.local;

import akka.actor.AbstractActor;
import com.framework.akka_router.Dispatch;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequest;
import com.framework.message.MessageBus;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor implements Dispatch {

    private Internal.EnumLite routerId;

    public AbstractServiceActor(Internal.EnumLite routerId) {
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
        getSender().tell(message, getSelf());
    }

}