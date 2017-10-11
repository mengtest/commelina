package com.framework.akka_router.cluster.node;

import akka.actor.AbstractActor;
import com.framework.akka_router.DispatchForward;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.*;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor implements DispatchForward {

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
                .match(ApiRequestForward.class, this::onForward)
                .build();
    }

    public final void response(MessageBus message) {
        // 回复到 on request / on forward 的发送者那里
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

}