package com.game.gateway.router_v3;

import akka.actor.ActorRef;
import com.framework.akka_router.ApiRequestForwardEntity;
import com.framework.akka_router.cluster.RouterFrontedClusterActor;
import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/10/11.
 */
public class RoomRouterFrontedClusterActor extends RouterFrontedClusterActor {

    public RoomRouterFrontedClusterActor(Internal.EnumLite myRouterId) {
        super(myRouterId);
    }

    @Override
    public Internal.EnumLite selectActorSeed(ApiRequest apiRequest) {
        return super.selectActorSeed(apiRequest);
    }

    @Override
    public Internal.EnumLite selectActorSeed(ApiRequestForwardEntity requestForward) {
        return super.selectActorSeed(requestForward);
    }

    @Override
    public void onForward(ApiRequestForwardEntity request, ActorRef target) {
        super.onForward(request, target);
    }
}