package com.framework.akka_router;

import akka.actor.AbstractActor;
import com.framework.message.ApiRequest;

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

}
