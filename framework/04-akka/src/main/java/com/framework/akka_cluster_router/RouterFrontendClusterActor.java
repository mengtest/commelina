package com.framework.akka_cluster_router;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
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

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClusterRouterJoinEntity.class, j -> {
                    ActorRef target = clusterRouters.get(j.getRouterId());
                    if (target == null) {
                        sender().tell(new RouterNotFoundEntity(j.getRouterId()), getSelf());
                    } else {
                        // 重定向到远程的 seed node 上，它自己再做 router
                        target.forward(j, getContext());
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
                .match(ClusterRouterRegistrationEntity.class, r -> {
                    getContext().watch(sender());
                    clusterRouters.put(r.getRouterId(), sender());
                })
                .match(Terminated.class, terminated -> {
                    clusterRouters.inverse().remove(terminated.getActor());
                })
                .build();
    }

}
