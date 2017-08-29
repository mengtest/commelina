package com.instruction.matching.portal;

import akka.actor.AbstractActor;
import com.nexus.maven.core.message.*;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class RouterActor extends AbstractActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequestWithLogin.class, this::onRequest)
                .match(ResponseMessage.class, r -> getSender().tell(r, getSelf()))
                .match(NotifyMessage.class, n -> getSender().tell(n, getSelf()))
                .match(BroadcastMessage.class, b -> getSender().tell(b, getSelf()))
                .match(WorldMessage.class, w -> getSender().tell(w, getSelf()))
                .build();
    }

    protected abstract void onRequest(ApiRequestWithLogin request);

}
