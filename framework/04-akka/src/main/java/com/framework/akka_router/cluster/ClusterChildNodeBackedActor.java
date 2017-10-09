package com.framework.akka_router.cluster;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Terminated;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import com.framework.akka_router.DispatchForward;
import com.framework.akka_router.Router;
import com.framework.akka_router.RouterForwardRegistrationEntity;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestForward;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class ClusterChildNodeBackedActor extends AbstractActor implements Router, DispatchForward {

    private final Cluster cluster = Cluster.get(getContext().system());

    private BiMap<Internal.EnumLite, ActorRef> localRouters = HashBiMap.create(16);
    private BiMap<Internal.EnumLite, ActorRef> forwardRouters = HashBiMap.create(16);

    //subscribe to cluster changes, MemberUp
    @Override
    public void preStart() {
        cluster.subscribe(self(), ClusterEvent.MemberUp.class, ClusterEvent.MemberRemoved.class);
    }

    //re-subscribe when restart
    @Override
    public void postStop() {
        cluster.unsubscribe(self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .match(ApiRequestForward.class, this::onForward)
                .match(ClusterEvent.CurrentClusterState.class, state -> {
                    for (Member member : state.getMembers()) {
                        if (member.status().equals(MemberStatus.up())) {
                            register(member);
                        } else if (member.status().equals(MemberStatus.removed())) {
                            remove(member);
                        }
                    }
                })
                .match(ClusterEvent.MemberUp.class, mUp -> register(mUp.member()))
                .match(ClusterEvent.MemberRemoved.class, mRem -> remove(mRem.member()))
                .match(RouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    localRouters.put(r.getRouterId(), sender());
                })
                .match(RouterForwardRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    forwardRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> {
                    if (localRouters.inverse().remove(terminated.getActor()) == null) {
                        forwardRouters.inverse().remove(terminated.getActor());
                    }
                })
                .build();
    }

    @Override
    public void onRequest(ApiRequest request) {
        ActorRef target = localRouters.get(request.getOpcode());
        if (target != null) {
            target.forward(request, getContext());
        } else {
            this.unhandled(request);
        }
    }

    @Override
    public void onForward(ApiRequestForward forward) {
        ActorRef target = forwardRouters.get(forward.getOpcode());
        if (target != null) {
            target.forward(forward, getContext());
        } else {
            this.unhandled(forward);
        }
    }


    void register(Member member) {
        if (member.hasRole("frontend")) {
            ActorSelection clusterFronted = getContext().actorSelection(member.address() + "/user/clusterRouterFronted");
            clusterFronted.tell(new RouterRegistrationEntity(getRouterId()), self());
            ClusterChildNodeSystem.INSTANCE.registerRouterFronted(clusterFronted);
        }
    }

    void remove(Member member) {
        if (member.hasRole("frontend")) {
            ClusterChildNodeSystem.INSTANCE.removeRouterFronted();
        }
    }

}
