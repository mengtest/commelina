package com.framework.akka.router.local;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka.router.Dispatch;
import com.framework.akka.router.proto.ApiRequest;
import com.framework.akka.router.proto.RouterRegistration;
import com.framework.core.MessageBody;
import com.framework.niosocket.message.ResponseMessage;
import com.google.protobuf.Internal;

/**
 *
 * @author @panyao
 * @date 2017/9/25
 */
public abstract class AbstractLocalServiceActor extends AbstractActor implements Dispatch {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    private final Internal.EnumLite routerId;
    private final RouterRegistration routerRegistration;

    public AbstractLocalServiceActor(Internal.EnumLite routerId) {
        this.routerId = routerId;
        routerRegistration = RouterRegistration.newBuilder().setRouterId(routerId.getNumber()).build();
    }

    @Override
    public void preStart() throws Exception {
        AkkaLocalWorkerSystem.INSTANCE.localRouterRegister(routerRegistration, getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    public final void response(MessageBody message) {
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }
}