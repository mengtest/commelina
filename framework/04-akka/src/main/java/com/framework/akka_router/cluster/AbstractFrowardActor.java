package com.framework.akka_router.cluster;

import com.framework.akka_router.RouterForwardRegistrationEntity;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class AbstractFrowardActor extends AbstractServiceActor{

    public AbstractFrowardActor(Internal.EnumLite routerId) {
        super(routerId);
    }

    @Override
    public void preStart() throws Exception {
        ClusterChildNodeSystem.INSTANCE.localRouterRegister(new RouterForwardRegistrationEntity(routerId), getSelf());
    }

}