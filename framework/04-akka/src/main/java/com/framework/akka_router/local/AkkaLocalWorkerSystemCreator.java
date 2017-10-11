package com.framework.akka_router.local;

import akka.actor.Props;
import com.framework.akka_router.LocalServiceHandler;

import java.util.Map;

/**
 * Created by @panyao on 2017/9/30.
 */
public final class AkkaLocalWorkerSystemCreator {

    public void registerRouter(Class<? extends RouterFrontendLocalActor> localActorClass, Map<String, LocalServiceHandler> routers) {
        AkkaLocalWorkerSystem.INSTANCE.registerRouterFronted(Props.create(localActorClass));
        for (LocalServiceHandler handler : routers.values()) {
            AkkaLocalWorkerSystem.INSTANCE.getSystem().actorOf(Props.create(handler.getPropsClass(), handler.getRouterId()));
        }
    }

    public void registerRouter(Map<String, LocalServiceHandler> routers) {
        registerRouter(RouterFrontendLocalActor.class, routers);
    }

    public static AkkaLocalWorkerSystemCreator create(String akkaActorConfig) {
        AkkaLocalWorkerSystem.INSTANCE.create(akkaActorConfig);
        return new AkkaLocalWorkerSystemCreator();
    }

    public static AkkaLocalWorkerSystemCreator create() {
        AkkaLocalWorkerSystem.INSTANCE.create();
        return new AkkaLocalWorkerSystemCreator();
    }

    AkkaLocalWorkerSystemCreator() {

    }

}
