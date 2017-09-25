package com.framework.akka_router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterFrontendActor extends AbstractActor {

    private final BiMap<Internal.EnumLite, ActorRef> clusterRouters = HashBiMap.create(4);
    private BiMap<Internal.EnumLite, ActorRef> localRouters = HashBiMap.create(16);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClusterRouterJoinEntity.class, j -> {
                    ActorRef target = clusterRouters.get(j.getRouterId());
                    if (target == null) {
                        sender().tell(new RouterNotFoundEntity(j.getRouterId()), getSelf());
                    } else {
                        target.forward(j, getContext());
                    }
                })
                .match(LocalRouterJoinEntity.class, j -> {
                    ActorRef target = localRouters.get(j.getRouterId());
                    if (target == null) {
                        sender().tell(new RouterNotFoundEntity(j.getRouterId()), getSelf());
                    } else {
                        target.forward(j, getContext());
                    }
                })
                .match(ClusterRouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    clusterRouters.put(r.getRouterId(), sender());
                })
                .match(LocalRouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    localRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> {
                    Internal.EnumLite routerId = clusterRouters.inverse().remove(terminated.getActor());
                    if (routerId == null) {
                        localRouters.inverse().remove(terminated.getActor());
                    }
                })
                .build();
    }

}
