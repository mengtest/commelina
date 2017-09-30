package com.framework.akka_router;

import com.framework.message.ApiRequest;
import com.google.protobuf.Internal;
import scala.concurrent.Future;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class DefaultLocalActorRequestHandler extends AbstractActorRequestHandler {

    @Override
    protected final Future<Object> ask(Internal.EnumLite routerId, ApiRequest apiRequest) {
        return AkkaWorkerSystem.INSTANCE.askRouterLocalNode(routerId, apiRequest);
    }

}