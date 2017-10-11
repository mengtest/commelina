package com.framework.akka_router.cluster.node;

import akka.actor.Props;

/**
 * Created by @panyao on 2017/9/30.
 */
public class ClusterChildNodeSystemCreator {

    public static ClusterChildNodeSystemCreator create(Class<? extends ClusterChildNodeBackedActor> backedClass, String akkaActorConfig) {
        ClusterChildNodeSystem.INSTANCE.create(akkaActorConfig);
        ClusterChildNodeSystem.INSTANCE.registerRouterFronted(Props.create(backedClass));
        return new ClusterChildNodeSystemCreator();
    }

    public static ClusterChildNodeSystemCreator create(Class<? extends ClusterChildNodeBackedActor> backedClass) {
        ClusterChildNodeSystem.INSTANCE.create();
        ClusterChildNodeSystem.INSTANCE.registerRouterFronted(Props.create(backedClass));
        return new ClusterChildNodeSystemCreator();
    }

    ClusterChildNodeSystemCreator() {

    }

}
