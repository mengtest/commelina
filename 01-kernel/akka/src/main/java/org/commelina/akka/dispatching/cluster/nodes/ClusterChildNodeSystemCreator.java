package org.commelina.akka.dispatching.cluster.nodes;

import akka.actor.Props;

/**
 *
 * @author @panyao
 * @date 2017/9/30
 */
public class ClusterChildNodeSystemCreator {

    public static ClusterChildNodeSystemCreator create(
            Class<? extends BackendActor> backedClass,
            String clusterName,
            String akkaActorConfig) {
        ClusterChildNodeSystem.INSTANCE.create(clusterName, akkaActorConfig);
        ClusterChildNodeSystem.INSTANCE.registerRouterFronted(Props.create(backedClass));
        return new ClusterChildNodeSystemCreator();
    }

    ClusterChildNodeSystemCreator() {

    }

}
