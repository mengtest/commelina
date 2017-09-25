package com.framework.akka_cluster_router;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class ClusterChildNodeBackedActor extends AbstractActor implements Router, RequestHandlerWatching {

    private Cluster cluster = Cluster.get(getContext().system());

    private ActorSelection clusterFronted;

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
                .build();
    }

    void register(Member member) {
        if (member.hasRole("frontend")) {
            clusterFronted = getContext().actorSelection(member.address() + "/user/routerFronted");
            clusterFronted.tell(new ClusterRouterRegistrationEntity(this.getRouterId(), (byte) 0), self());
        }
    }
//
//    /**
//     * @return
//     */
//    protected abstract String getFronted();

}
