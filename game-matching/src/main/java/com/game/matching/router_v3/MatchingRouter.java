package com.game.matching.router_v3;

import com.framework.akka_router.cluster.ClusterChildNodeBackedActor;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/26.
 */
public class MatchingRouter extends ClusterChildNodeBackedActor {

    @Override
    public Internal.EnumLite getRouterId() {
        return null;
    }

    @Override
    public void onRequest(ApiRequest request) {

    }

    @Override
    public void onForward(ApiRequestForward forward) {

    }

}