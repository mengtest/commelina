package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class AbstractServerRouterActorRequest extends AbstractActor implements ActorRequestWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

}
