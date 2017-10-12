package com.framework.akka_router.local;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequest;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterFrontendLocalActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    private BiMap<Integer, ActorRef> localRouters = HashBiMap.create(16);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DeadLetter.class, d -> {
                    // 死信，防止一个 front node 崩溃之后 service actor 成为游离状态
                    if (d.message() instanceof RouterRegistrationEntity) {
                        getContext().watch(d.sender());
                        localRouters.put(((RouterRegistrationEntity) d.message()).getRouterId().getNumber(), d.sender());
                    } else if (d.message() instanceof ApiRequest) {
                        logger.info("ignore. {}", d.message());
                    } else {
                        unhandled(d);
                    }
                })
                .match(ApiRequest.class, r -> {
                    ActorRef target = localRouters.get(r.getOpcode().getNumber());
                    if (target != null) {
                        target.forward(r, getContext());
                    } else {
                        this.unhandled(r);
                    }
                })
                .match(RouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    localRouters.put(r.getRouterId().getNumber(), sender());
                })
                .match(Terminated.class, terminated -> localRouters.inverse().remove(terminated.getActor()))
                .build();
    }


}
