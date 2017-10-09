package com.game.matching.router_v3;

import com.framework.akka_router.cluster.ClusterChildNodeBackedActor;
import com.framework.message.ApiRequest;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/26.
 */
public class MatchingRouter extends ClusterChildNodeBackedActor {

    @Override
    public Internal.EnumLite getRouterId() {
        return () -> 0;
    }

    @Override
    public void onRequest(ApiRequest request) {

    }

    @Override
    public void onForward(ApiRequestForwardEntity forward) {

    }

}