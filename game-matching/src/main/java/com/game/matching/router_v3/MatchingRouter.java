package com.game.matching.router_v3;

import com.framework.akka_router.ClusterChildNodeBackedActor;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/26.
 */
public class MatchingRouter extends ClusterChildNodeBackedActor {

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

}