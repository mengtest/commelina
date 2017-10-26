package com.github.freedompy.commelina.akka.router.cluster;

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
import com.github.freedompy.commelina.akka.router.Rewrite;
import com.github.freedompy.commelina.akka.router.cluster.nodes.ClusterChildNodeSystem;
import com.github.freedompy.commelina.akka.router.proto.*;
import com.github.freedompy.commelina.niosocket.MessageAdapter;
import com.github.freedompy.commelina.niosocket.message.BroadcastMessage;
import com.github.freedompy.commelina.niosocket.message.NotifyMessage;
import com.github.freedompy.commelina.niosocket.message.WorldMessage;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

import java.util.concurrent.CompletableFuture;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public class RouterFrontedClusterActor extends AbstractActor implements Rewrite {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    private final BiMap<Integer, ActorRef> clusterNodeRouters = HashBiMap.create(8);

    private final Internal.EnumLite myRouterId;

    private Cluster cluster = Cluster.get(getContext().system());

    public RouterFrontedClusterActor(Internal.EnumLite myRouterId) {
        this.myRouterId = myRouterId;
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
                    // 重定向到远程的 seed nodes 上，它自己再做 router
                    ActorRef target = clusterNodeRouters.get(selectActorSeed(r));
                    if (target != null) {
                        // 重定向到远程的 seed nodes 上，它自己再做 router
                        target.forward(r, getContext());
                    } else {
                        unhandled(r);
                    }
                })
                // from cluster seed nodes.
                .match(ActorNotify.class, n -> MessageAdapter.sendNotify(myRouterId, NotifyMessage.newMessage(
                        n.getOpcode(),
                        n.getUserId(),
                        () -> n.getMessage().toByteArray()
                )))
                .match(ActorBroadcast.class, b -> MessageAdapter.sendBroadcast(myRouterId, BroadcastMessage.newBroadcast(
                        b.getOpcode(), b.getUserIdsList(), () -> b.getMessage().toByteArray()
                )))
                .match(ActorWorld.class, w -> MessageAdapter.sendWorld(myRouterId, WorldMessage.newMessage(
                        w.getOpcode(), () -> w.getMessage().toByteArray())))
                // 重定向请求
                .match(ApiRequestForward.class, rf -> {
                    AkkaMultiWorkerSystem targetSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(rf.getForward());
                    if (targetSystem != null) {
                        // 重定向到远程的 seed nodes 上，它自己再做 router
                        ActorRef target = clusterNodeRouters.get(selectActorSeed(rf));
                        if (target != null) {
                            // https://doc.akka.io/docs/akka/current/java/actors.html#ask-send-and-receive-future
                            // 向远程 发起 ask 请求
                            CompletableFuture<Object> askFuture = PatternsCS.ask(target, rf, ClusterChildNodeSystem.DEFAULT_TIMEOUT)
                                    .toCompletableFuture();

                            // ask with pipe
                            CompletableFuture<ActorResponse> transformed = CompletableFuture
                                    .allOf(askFuture)
                                    .thenApply(v -> (ActorResponse) askFuture.join());

                            // ask with pipe to sender.
                            PatternsCS.pipe(transformed, getContext().getSystem().dispatcher()).to(getSender(), getSelf());
                        } else {
                            unhandled(rf);
                        }
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
                .build();
    }

    @Override
    public int selectActorSeed(ApiRequest ask) {
        return clusterNodeRouters.keySet().iterator().next();
    }

    @Override
    public int selectActorSeed(ApiRequestForward forward) {
        return clusterNodeRouters.keySet().iterator().next();
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

}