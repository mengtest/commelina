package com.github.freedompy.commelina.akka.router.cluster;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 *
 * @author @panyao
 * @date 2017/9/25
 * <p>
 * 集群工作 actor system，一个功能一个集群
 */
public class AkkaMultiWorkerSystemContext {

    public static final AkkaMultiWorkerSystemContext INSTANCE = new AkkaMultiWorkerSystemContext();

    private final BiMap<Integer, AkkaMultiWorkerSystem> clusterSystems = HashBiMap.create(4);

    public AkkaMultiWorkerSystem getContext(int routerId) {
        return clusterSystems.get(routerId);
    }

    void registerWorkerSystem(int routerId, AkkaMultiWorkerSystem systemV3) {
        clusterSystems.put(routerId, systemV3);
    }

}