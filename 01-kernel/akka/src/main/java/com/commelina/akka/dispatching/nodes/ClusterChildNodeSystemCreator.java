package com.commelina.akka.dispatching.nodes;

/**
 *
 * @author @panyao
 * @date 2017/9/30
 */
public class ClusterChildNodeSystemCreator {

    public static ClusterChildNodeSystemCreator create(
            Class<? extends AbstractBackendActor> backedClass,
            String clusterName,
            String akkaActorConfig) {
        ClusterChildNodeSystem.INSTANCE.create(clusterName, akkaActorConfig);
        return new ClusterChildNodeSystemCreator();
    }

    ClusterChildNodeSystemCreator() {

    }

}
