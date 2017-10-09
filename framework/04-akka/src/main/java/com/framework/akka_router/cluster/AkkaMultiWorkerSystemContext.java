package com.framework.akka_router.cluster;

import akka.util.Timeout;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.protobuf.Internal;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/9/25.
 * <p>
 * 集群工作 actor system，一个功能一个集群
 */
public class AkkaMultiWorkerSystemContext {

    public static final AkkaMultiWorkerSystemContext INSTANCE = new AkkaMultiWorkerSystemContext();

    private final BiMap<Internal.EnumLite, AkkaMultiWorkerSystemV3> clusterSystems = HashBiMap.create(4);

    public static final Timeout defaultTimeout = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public AkkaMultiWorkerSystemV3 getContext(Internal.EnumLite routerId) {
        return clusterSystems.get(routerId);
    }

    void registerWorkerSystem(Internal.EnumLite routerId, AkkaMultiWorkerSystemV3 systemV3) {
        clusterSystems.put(routerId, systemV3);
    }

//    public Future<Object> askRouterClusterNode(Internal.EnumLite routerId, ApiRequest apiRequest) {
//        return askRouterClusterNode(routerId, apiRequest, defaultTimeout);
//    }
//
//    public Future<Object> askRouterClusterNode(Internal.EnumLite routerId, ApiRequest apiRequest, Timeout timeout) {
//        return Patterns.ask(clusterRouterFronted, new RouterJoinEntity(routerId, apiRequest), timeout);
//    }

}