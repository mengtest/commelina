package com.framework.akka_router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;
import com.framework.niosocket.MessageAdapter;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public class RouterFrontendClusterActor extends AbstractActor {

    private final BiMap<Internal.EnumLite, ActorRef> clusterRouters = HashBiMap.create(4);

    private final ServerRequestForwardHandler forwardHandler;

    public RouterFrontendClusterActor(ServerRequestForwardHandler forwardHandler) {
        this.forwardHandler = forwardHandler;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 客户端请求
                .match(ClusterRouterJoinEntity.class, j -> {
                    ActorRef target = clusterRouters.get(j.getRouterId());
                    if (target != null) {
                        // 重定向到远程的 seed node 上，它自己再做 router
                        target.forward(j, getContext());
                    } else {
                        unhandled(j);
                    }
                })
                // from cluster seed node.
                .match(BroadcastMessage.class, b -> {
                    // 获取 router id
                    MessageAdapter.addBroadcast(clusterRouters.inverse().get(getSender()), b);
                })
                .match(NotifyMessage.class, n -> {
                    // 获取 router id
                    MessageAdapter.addNotify(clusterRouters.inverse().get(getSender()), n);
                })
                .match(WorldMessage.class, w -> {
                    // 获取 router id
                    MessageAdapter.addWorld(clusterRouters.inverse().get(getSender()), w);
                })
                // server 请求 重定向， 如 matching -> room
                .match(ServerRequestForwardEntity.class, f -> {
                    forwardHandler.onRequest(f.getForwardId(), f.getRequestForward(), getSender());
                })
                .match(ClusterRouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    clusterRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> {
                    clusterRouters.inverse().remove(terminated.getActor());
                })
                .build();
    }

    public static Props props(ServerRequestForwardHandler forwardHandler) {
        return Props.create(RouterFrontendClusterActor.class, forwardHandler);
    }

}