package com.framework.akka_router.local;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.DeadLetter;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.RouterRegistration;
import com.framework.niosocket.proto.SocketASK;
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
                    if (d.message() instanceof RouterRegistration) {
                        getContext().watch(d.sender());
                        localRouters.put(((RouterRegistration) d.message()).getRouterId(), d.sender());
                    } else if (d.message() instanceof SocketASK) {
                        logger.info("ignore. {}", d.message());
                    } else {
                        unhandled(d);
                    }
                })
                .match(SocketASK.class, r -> {
                    ActorRef target = localRouters.get(r.getOpcode());
                    if (target != null) {
                        target.forward(r, getContext());
                    } else {
                        this.unhandled(r);
                    }
                })
                .match(RouterRegistration.class, r -> {
                    getContext().watch(sender());
                    localRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> localRouters.inverse().remove(terminated.getActor()))
                .build();
    }


}
