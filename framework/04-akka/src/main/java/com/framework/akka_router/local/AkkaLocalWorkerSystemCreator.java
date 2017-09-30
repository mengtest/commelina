package com.framework.akka_router.local;

import akka.actor.Props;
import com.framework.akka_router.RouterRegistrationEntity;
import com.framework.akka_router.ServiceHandler;

import java.util.Map;

/**
 * Created by @panyao on 2017/9/30.
 */
public final class AkkaLocalWorkerSystemCreator {

    public void registerRouter(Class<? extends RouterFrontendLocalActor> localActorClass, Map<String, ServiceHandler> routers) {
        AkkaLocalWorkerSystem.INSTANCE.registerLocal(Props.create(localActorClass));
        for (ServiceHandler handler : routers.values()) {
            AkkaLocalWorkerSystem.INSTANCE.localRouterRegister(new RouterRegistrationEntity(handler.getRouterId()), handler.getProps());
        }
    }

    public static AkkaLocalWorkerSystemCreator create(String akkaActorConfig) {
        AkkaLocalWorkerSystem.INSTANCE.create(akkaActorConfig);
        return new AkkaLocalWorkerSystemCreator();
    }

    private AkkaLocalWorkerSystemCreator() {

    }

}
