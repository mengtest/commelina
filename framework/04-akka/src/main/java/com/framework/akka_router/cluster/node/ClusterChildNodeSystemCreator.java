package com.framework.akka_router.cluster.node;

import akka.actor.Props;

/**
 * Created by @panyao on 2017/9/30.
 */
public class ClusterChildNodeSystemCreator {

    public static ClusterChildNodeSystemCreator create(
            Class<? extends ClusterChildNodeBackedActor> backedClass,
            String clusterName,
            String akkaActorConfig) {
        ClusterChildNodeSystem.INSTANCE.create(clusterName, akkaActorConfig);
        ClusterChildNodeSystem.INSTANCE.registerRouterFronted(Props.create(backedClass));
        return new ClusterChildNodeSystemCreator();
    }

    ClusterChildNodeSystemCreator() {

    }

}
