package com.framework.akka_router.cluster.node;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.*;
import com.framework.core.MessageBody;
import com.framework.niosocket.message.ResponseMessage;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class BackedActor extends AbstractActor implements Router, DispatchForward, MemberEvent {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    private final Cluster cluster = Cluster.get(getContext().system());

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
                .match(MemberOfflineEvent.class, off -> onOffline(off.getLogoutUserId()))
                .match(MemberOnlineEvent.class, on -> onOnline(on.getLoginUserId()))
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
//                .match(Terminated.class, terminated -> {})
                .build();
    }

    @Override
    public void onOnline(long logoutUserId) {
        // nothing to do
    }

    @Override
    public void onOffline(long logoutUserId) {
        // nothing to do
    }

    @Override
    public void onForward(ApiRequestForward forward) {
        // nothing to do
    }

    protected void response(MessageBody message) {
        getSender().tell(ResponseMessage.newMessage(message), getSelf());
    }

    void register(Member member) {
        if (member.hasRole("frontend")) {
            ActorSelection clusterFronted = getContext().actorSelection(member.address() + "/user/clusterRouterFrontend");
            clusterFronted.tell(RouterRegistration.newBuilder().setRouterId(getRouterId().getNumber()).build(), self());
            ClusterChildNodeSystem.INSTANCE.registerRouterFronted(clusterFronted);
        }
    }

    void remove(Member member) {
        if (member.hasRole("frontend")) {
            ClusterChildNodeSystem.INSTANCE.removeRouterFronted();
        }
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }

}
