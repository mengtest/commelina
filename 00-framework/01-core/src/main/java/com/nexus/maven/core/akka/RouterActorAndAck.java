package com.nexus.maven.core.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import com.google.common.collect.Maps;
import com.nexus.maven.core.message.*;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/29.
 * @deprecated
 */
public abstract class RouterActorAndAck extends AbstractActor {

    private long ackId = 0;

    private final Map<Long, ActorRef> actors = Maps.newHashMap();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequestWithLogin.class, this::onRequest)
                .match(ResponseMessage.class, r -> getSender().tell(r, getSelf()))
                .match(ActorRouterResponseMessageAck.class, r -> {
                    if (ackId >= Long.MAX_VALUE) {
                        ackId = 0;
                    }

                    ResponseMessageAck responseMessageAck = ResponseMessageAck.newNotifyAck(ackId++, r);
                    getSender().tell(responseMessageAck, getSelf());
                })
                .match(NotifyMessage.class, n -> getSender().tell(n, getSelf()))
                .match(BroadcastMessage.class, b -> getSender().tell(b, getSelf()))
                .match(WorldMessage.class, w -> getSender().tell(w, getSelf()))
                .match(ACKMessage.class, a -> {
                    ActorRef actorRef = actors.get(a.getAckId());
                    if (actorRef != null) {

                    }
                })
                .build();
    }

    protected abstract void onRequest(ApiRequestWithLogin request);

}
