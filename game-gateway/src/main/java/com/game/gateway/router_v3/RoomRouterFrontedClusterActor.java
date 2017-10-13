package com.game.gateway.router_v3;

import com.framework.akka_router.cluster.RouterFrontedClusterActor;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/10/11.
 */
public class RoomRouterFrontedClusterActor extends RouterFrontedClusterActor {

    public RoomRouterFrontedClusterActor(Internal.EnumLite myRouterId) {
        super(myRouterId);
    }

}