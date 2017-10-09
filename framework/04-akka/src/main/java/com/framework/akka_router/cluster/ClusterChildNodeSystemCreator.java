package com.framework.akka_router.cluster;

import akka.actor.Props;
import com.framework.akka_router.local.AkkaLocalWorkerSystem;

import java.util.Map;

/**
 * Created by @panyao on 2017/9/30.
 */
public class ClusterChildNodeSystemCreator {

    public void registerServiceRouter(Map<String, ServiceHandler> routers) {
        for (ServiceHandler handler : routers.values()) {
            AkkaLocalWorkerSystem.INSTANCE.getSystem().actorOf(Props.create(handler.getPropsClass(), handler.getRouterId()));
        }
    }

    public void registerForwardRouter(Map<String, ForwardHandler> routers) {
        for (ServiceHandler handler : routers.values()) {
            AkkaLocalWorkerSystem.INSTANCE.getSystem().actorOf(Props.create(handler.getPropsClass(), handler.getRouterId()));
        }
    }

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
