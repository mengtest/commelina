package com.game.matching.router_v3;

import com.framework.akka_router.ActorServiceHandler;
import com.framework.akka_router.ServiceHandler;
import com.framework.akka_router.local.AbstractLocalServiceActor;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/10/9.
 */
@ActorServiceHandler
public class MatchingServiceRouter implements ServiceHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

    @Override
    public Class<? extends AbstractLocalServiceActor> getPropsClass() {
        return null;
    }

}
