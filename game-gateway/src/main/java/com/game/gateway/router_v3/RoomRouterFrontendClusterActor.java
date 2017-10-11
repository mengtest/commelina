package com.game.gateway.router_v3;

import com.framework.akka_router.cluster.RouterFrontendClusterActor;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/10/11.
 */
public class RoomRouterFrontendClusterActor extends RouterFrontendClusterActor {

    public RoomRouterFrontendClusterActor(Internal.EnumLite myRouterId) {
        super(myRouterId);
    }

}
