package com.framework.akka_router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Terminated;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import com.framework.message.ApiRequest;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class ClusterChildNodeBackedActor extends AbstractActor implements Router, Dispatch {

    private final Cluster cluster = Cluster.get(getContext().system());

    private ActorSelection clusterFronted;

    private BiMap<Internal.EnumLite, ActorRef> localRouters = HashBiMap.create(16);

    //subscribe to cluster changes, MemberUp
    @Override
    public void preStart() {
        cluster.subscribe(self(), ClusterEvent.MemberUp.class);
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
                .match(ClusterEvent.CurrentClusterState.class, state -> {
                    for (Member member : state.getMembers()) {
                        if (member.status().equals(MemberStatus.up())) {
                            register(member);
                        }
                    }
                })
                .match(ClusterEvent.MemberUp.class, mUp -> register(mUp.member()))
                .match(LocalRouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    localRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> {
                    localRouters.inverse().remove(terminated.getActor());
                })
                .build();
    }

    void register(Member member) {
        if (member.hasRole("frontend")) {
            clusterFronted = getContext().actorSelection(member.address() + "/user/routerFronted");
            clusterFronted.tell(new ClusterRouterRegistrationEntity(getRouterId(), (byte) 0), self());
        }
    }

    @Override
    public final void onRequest(ApiRequest request) {

    }

    //
//    /**
//     * @return
//     */
//    protected abstract String getFronted();

}