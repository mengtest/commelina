package com.framework.akka_router.cluster;

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

    protected Internal.EnumLite routerId;

    public AbstractServiceActor(Internal.EnumLite routerId) {
        this.routerId = routerId;
    }

    @Override
    public void preStart() throws Exception {
        ClusterChildNodeSystem.INSTANCE.localRouterRegister(new RouterRegistrationEntity(routerId), getSelf());
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