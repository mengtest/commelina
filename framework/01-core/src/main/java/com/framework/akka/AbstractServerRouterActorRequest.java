package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.ApiRouterRequest;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class AbstractServerRouterActor extends AbstractActor implements ActorWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRouterRequest.class, this::onRequest)
                .build();
    }

}
