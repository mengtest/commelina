package com.framework.akka_router;

import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;
import scala.concurrent.Future;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class DefaultClusterActorRequestHandler extends AbstractActorRequestHandler {

    @Override
    protected Future<Object> ask(Internal.EnumLite routerId, ApiRequest apiRequest) {
        return AkkaWorkerSystem.INSTANCE.askRouterClusterNode(routerId, apiRequest);
    }

}