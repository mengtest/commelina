package com.commelina.akka.dispatching;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.concurrent.CompletableFuture;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public abstract class AbstractClusterFrontendActor extends AbstractActor implements Rewrite, BackendEvent {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    private final BiMap<Integer, ActorRef> clusterNodeRouters = HashBiMap.create(8);

    private final int domain;

    private final Timeout timeout;

    private Cluster cluster = Cluster.get(getContext().system());

    public AbstractClusterFrontendActor(int domain, Timeout timeout) {
        this.domain = domain;
        this.timeout = timeout;
    }

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
    public final Receive createReceive() {
        return receiveBuilder()
                // 客户端请求
                .match(ApiRequest.class, r -> {
                    // 重定向到远程的 seed nodes 上，它自己再做 dispatching
                    ActorRef target = selectActor(r, clusterNodeRouters);
                    if (target != null) {
                        // 重定向到远程的 seed nodes 上，它自己再做 dispatching
                        target.forward(r, getContext());
                    } else {
                        unhandled(r);
                    }
                })
                // from cluster seed nodes.
                .match(ActorNotify.class, this::event)
                .match(ActorBroadcast.class, this::event)
                .match(ActorWorld.class, this::event)
                // 重定向请求
                .match(ApiRequestForward.class, rf -> {
                    // eg: gateway -> room
                    // 重定向到远程的 seed nodes 上，它自己再做 dispatching
                    ActorRef target = selectActor(rf, clusterNodeRouters);
                    if (target != null) {
                        // https://doc.akka.io/docs/akka/current/java/actors.html#ask-send-and-receive-future
                        // 向远程 发起 askForBackend 请求
                        CompletableFuture<Object> askFuture = PatternsCS.ask(target, rf, timeout)
                                .toCompletableFuture();

                        // askForBackend with pipe
                        CompletableFuture<ActorResponse> transformed = CompletableFuture
                                .allOf(askFuture)
                                .thenApply(v -> (ActorResponse) askFuture.join());

                        // askForBackend with pipe to sender.
                        PatternsCS.pipe(transformed, getContext().getSystem().dispatcher()).to(getSender(), getSelf());
                    } else {
                        unhandled(rf);
                    }
                })
                .match(Terminated.class, t -> {
//                    getContext().unwatch(t.getActor());
//                    clusterNodeRouters.inverse().remove(t.getActor());
                    logger.info("Remote backend {} left.", t.getActor());
                })
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
                .match(ActorDebugMessage.class, this::remoteDebug)
                .build();
    }

    protected void remoteDebug(ActorDebugMessage message) {
        unhandled(message);
    }

    @Override
    public ActorRef selectActor(ApiRequest ask, BiMap<Integer, ActorRef> clusterNodeRouters) {
        return clusterNodeRouters.values().iterator().next();
    }

    @Override
    public ActorRef selectActor(ApiRequestForward forward, BiMap<Integer, ActorRef> clusterNodeRouters) {
        return clusterNodeRouters.values().iterator().next();
    }

    void register(Member member) {
        if (member.hasRole(Constants.CLUSTER_BACKEND)) {
            logger.info("Remote port:{} , nodes register.", member.address().port().get());
            getContext().watch(sender());

            String portStr = member.address().port().get().toString();
            clusterNodeRouters.put(Integer.valueOf(portStr.substring(portStr.length() - 2)), sender());
        }
    }

    void remove(Member member) {
        if (member.hasRole(Constants.CLUSTER_BACKEND)) {
            logger.info("Remote port:{} , nodes remove.", member.address().port().get());
            getContext().unwatch(sender());
            clusterNodeRouters.inverse().remove(getSender());
        }
    }

    protected int getDomain() {
        return domain;
    }

}