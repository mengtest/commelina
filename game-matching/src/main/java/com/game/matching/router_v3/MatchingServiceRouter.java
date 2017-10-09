package com.game.matching.router_v3;

import com.framework.akka_router.cluster.ServiceHandler;
import com.framework.akka_router.local.AbstractServiceActor;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/10/9.
 */
public class MatchingServiceRouter implements ServiceHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

    @Override
    public Class<? extends AbstractServiceActor> getPropsClass() {
        return null;
    }

}
