package com.framework.akka_router;

import akka.actor.Props;
import com.google.protobuf.Internal;

import java.util.Map;

/**
 * Created by @panyao on 2017/9/30.
 */
public final class AkkaWorkerSystemCreator {

    public static void registerLocal(Class<? extends RouterFrontendLocalActor> localActorClass, Map<String, ServiceHandler> routers) {
        AkkaWorkerSystem.INSTANCE.registerLocal(Props.create(localActorClass));
        for (ServiceHandler handler : routers.values()) {
            AkkaWorkerSystem.INSTANCE.localRouterRegister(new RouterRegistrationEntity(handler.getRouterId()), handler.getProps());
        }
    }

    public static void registerCluster(Internal.EnumLite routerId, Class<? extends RouterFrontendClusterActor> clusterActorClass) {
        AkkaWorkerSystem.INSTANCE.registerCluster(routerId, Props.create(clusterActorClass, routerId));
    }

    public static void create(String akkaActorConfig) {
        AkkaWorkerSystem.INSTANCE.create(akkaActorConfig);
    }

}
