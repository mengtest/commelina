package com.framework.akka_router.cluster;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.PatternsCS;
import com.framework.akka_router.ActorResponse;
import com.framework.akka_router.ApiRequestForward;
import com.framework.akka_router.Rewrite;
import com.framework.akka_router.RouterRegistration;
import com.framework.akka_router.cluster.node.ClusterChildNodeSystem;
import com.framework.niosocket.message.NotifyMessage;
import com.framework.niosocket.proto.SocketASK;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

import java.util.concurrent.CompletableFuture;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterFrontedClusterActor extends AbstractActor implements Rewrite {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    private final BiMap<Integer, ActorRef> clusterNodeRouters = HashBiMap.create(8);

    private final Internal.EnumLite myRouterId;

    public RouterFrontedClusterActor(Internal.EnumLite myRouterId) {
        this.myRouterId = myRouterId;
    }

    @Override
    public final Receive createReceive() {
        return receiveBuilder()
                // 客户端请求
                .match(SocketASK.class, r -> {
                    // 重定向到远程的 seed node 上，它自己再做 router
                    ActorRef target = clusterNodeRouters.get(selectActorSeed(r).getNumber());
                    if (target != null) {
                        // 重定向到远程的 seed node 上，它自己再做 router
                        target.forward(r, getContext());
                    } else {
                        unhandled(r);
                    }
                })
                // from cluster seed node.
                .match(NotifyMessage.class, n -> {
//                    MessageAdapter.addNotify(myRouterId, n.);
                })
//                .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(myRouterId, b))
//                .match(WorldMessage.class, w -> MessageAdapter.addWorld(myRouterId, w))
                // 重定向请求
                .match(ApiRequestForward.class, rf -> {
                    AkkaMultiWorkerSystem targetSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(() -> rf.getForward());
                    if (targetSystem != null) {
                        // 重定向到远程的 seed node 上，它自己再做 router
                        // 重定向到远程的 seed node 上，它自己再做 router
                        ActorRef target = clusterNodeRouters.get(selectActorSeed(rf).getNumber());
                        if (target != null) {
                            // https://doc.akka.io/docs/akka/current/java/actors.html#ask-send-and-receive-future
                            // 向远程 发起 ask 请求
                            CompletableFuture<Object> askFuture = PatternsCS
                                    .ask(target, rf, ClusterChildNodeSystem.DEFAULT_TIMEOUT)
                                    .toCompletableFuture();

                            // ask with pipe
                            CompletableFuture<ActorResponse> transformed = CompletableFuture
                                    .allOf(askFuture)
                                    .thenApply(v -> (ActorResponse) askFuture.join());

                            // ask with pipe to sender.
                            PatternsCS.pipe(transformed, getContext().getSystem().dispatcher()).to(getSender());
                        } else {
                            unhandled(rf);
                        }
                    } else {
                        unhandled(rf);
                    }
                })
                .match(RouterRegistration.class, r -> {
                    logger.info("Router Id:{} , node register.", r.getRouterId());
                    getContext().watch(sender());
                    clusterNodeRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> clusterNodeRouters.inverse().remove(terminated.getActor()))
                .build();
    }

    @Override
    public Internal.EnumLite selectActorSeed(SocketASK ask) {
        return DEFAULT;
    }

    @Override
    public Internal.EnumLite selectActorSeed(ApiRequestForward forward) {
        return DEFAULT;
    }

    private static final Internal.EnumLite DEFAULT = () -> 0;

    private void actorForward(Internal.EnumLite seed, Object msg) {
        ActorRef target = clusterNodeRouters.get(seed);
        if (target != null) {
            // 重定向到远程的 seed node 上，它自己再做 router
            target.forward(msg, getContext());
        } else {
            unhandled(msg);
        }
    }

}