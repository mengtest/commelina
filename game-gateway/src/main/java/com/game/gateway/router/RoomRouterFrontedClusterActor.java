package com.game.gateway.router;

import com.framework.akka.router.cluster.RouterFrontedClusterActor;
import com.google.protobuf.Internal;

/**
 *
 * @author @panyao
 * @date 2017/10/11
 */
public class RoomRouterFrontedClusterActor extends RouterFrontedClusterActor {

    public RoomRouterFrontedClusterActor(Internal.EnumLite myRouterId) {
        super(myRouterId);
    }

}