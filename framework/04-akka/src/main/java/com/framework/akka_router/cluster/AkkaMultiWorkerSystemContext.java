package com.framework.akka_router.cluster;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 * <p>
 * 集群工作 actor system，一个功能一个集群
 */
public class AkkaMultiWorkerSystemContext {

    public static final AkkaMultiWorkerSystemContext INSTANCE = new AkkaMultiWorkerSystemContext();

    private final BiMap<Internal.EnumLite, AkkaMultiWorkerSystemV3> clusterSystems = HashBiMap.create(4);

    public AkkaMultiWorkerSystemV3 getContext(Internal.EnumLite routerId) {
        return clusterSystems.get(routerId);
    }

    void registerWorkerSystem(Internal.EnumLite routerId, AkkaMultiWorkerSystemV3 systemV3) {
        clusterSystems.put(routerId, systemV3);
    }

}