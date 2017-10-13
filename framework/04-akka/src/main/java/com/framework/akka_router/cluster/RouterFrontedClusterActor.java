package com.framework.akka_router.cluster;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.framework.akka_router.Rewrite;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.message.*;
import com.framework.niosocket.MessageAdapter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterFrontedClusterActor extends AbstractActor implements Rewrite {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    private final BiMap<Internal.EnumLite, ActorRef> clusterNodeRouters = HashBiMap.create(8);

    private final Internal.EnumLite myRouterId;

    public RouterFrontedClusterActor(Internal.EnumLite myRouterId) {
        this.myRouterId = myRouterId;
    }

    @Override
    public final Receive createReceive() {
        return receiveBuilder()
                // 客户端请求
                .match(ApiRequest.class, r -> {
                    // 重定向到远程的 seed node 上，它自己再做 router
                    actorForward(selectActorSeed(r), r);
                })
                // from cluster seed node.
                .match(NotifyMessage.class, n -> MessageAdapter.addNotify(myRouterId, n))
                .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(myRouterId, b))
                .match(WorldMessage.class, w -> MessageAdapter.addWorld(myRouterId, w))
                // 重定向请求
                .match(ApiRequestForward.class, rf -> {
                    // 重定向到远程的 seed node 上，它自己再做 router
                    actorForward(selectActorSeed(rf), rf);
                })
                // server 请求 重定向， 如 matching -> room
                .match(ApiRequestForwardEntity.class, f -> {
                    AkkaMultiWorkerSystem targetSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(f.getForwardId());
                    if (targetSystem != null) {
                        // 向远程 发起 ask 请求
                        // 重定向到远程的 seed node 上，它自己再做 router
                        targetSystem.askRouterClusterNodeForward(f.getRequestForward(), getSender());
                    } else {
                        unhandled(f);
                    }
                })
                .match(RouterRegistrationEntity.class, r -> {
                    logger.info("Router Id:{} , node register.", r.getRouterId());
                    getContext().watch(sender());
                    clusterNodeRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> clusterNodeRouters.inverse().remove(terminated.getActor()))
                .build();
    }

    @Override
    public Internal.EnumLite selectActorSeed(ApiRequest apiRequest) {
        return DEFAULT;
    }

    @Override
    public Internal.EnumLite selectActorSeed(ApiRequestForward requestForward) {
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