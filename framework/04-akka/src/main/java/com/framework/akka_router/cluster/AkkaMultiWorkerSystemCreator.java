package com.framework.akka_router.cluster;

import akka.actor.Props;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 * <p>
 * 集群创建器
 */
public class AkkaMultiWorkerSystemCreator {

    private AkkaMultiWorkerSystem systemV3;
    private Internal.EnumLite routerId;

    public AkkaMultiWorkerSystemCreator registerRouter(Class<? extends RouterFrontendClusterActor> routerClass) {
        systemV3.registerRouterFronted(Props.create(routerClass, routerId));
        return this;
    }

    public AkkaMultiWorkerSystemCreator registerRouter() {
        return registerRouter(RouterFrontendClusterActor.class);
    }

    public void building() {
        AkkaMultiWorkerSystemContext.INSTANCE.registerWorkerSystem(routerId, systemV3);
    }

    public static AkkaMultiWorkerSystemCreator create(Internal.EnumLite routerId, String akkaActorConfig) {
        AkkaMultiWorkerSystemCreator creator = new AkkaMultiWorkerSystemCreator();
        creator.systemV3 = new AkkaMultiWorkerSystem();
        creator.systemV3.create(akkaActorConfig);
        creator.routerId = routerId;
        return creator;
    }

    public static AkkaMultiWorkerSystemCreator create(Internal.EnumLite routerId) {
        AkkaMultiWorkerSystemCreator creator = new AkkaMultiWorkerSystemCreator();
        creator.systemV3 = new AkkaMultiWorkerSystem();
        creator.systemV3.create();
        creator.routerId = routerId;
        return creator;
    }

    private AkkaMultiWorkerSystemCreator() {

    }

}