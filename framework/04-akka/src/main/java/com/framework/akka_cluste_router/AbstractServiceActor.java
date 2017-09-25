package com.framework.akka_cluste_router;

import akka.actor.AbstractActor;
import com.framework.message.*;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractServiceActor extends AbstractActor implements RequestHandlerWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    public final void reply(ResponseMessage message) {
        getSender().tell(message, getSelf());
    }

}
