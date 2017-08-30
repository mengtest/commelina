package com.nexus.maven.core.akka;

import akka.actor.AbstractActor;
import com.nexus.maven.core.message.*;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class RouterActor extends AbstractActor implements RouterActorWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequestWithActor.class, (r) -> {
                    if (!this.onRequest(r)) {
                        this.unhandled(r);
                    }
                })
                .match(ResponseMessage.class, r -> getSender().tell(r, getSelf()))
                .match(NotifyMessage.class, n -> getSender().tell(n, getSelf()))
                .match(BroadcastMessage.class, b -> getSender().tell(b, getSelf()))
                .match(WorldMessage.class, w -> getSender().tell(w, getSelf()))
                .match(MemberOnlineEvent.class, this::onOnline)
                .match(MemberOfflineEvent.class, this::onOffline)
                .build();
    }

}
