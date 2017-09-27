package com.framework.akka_router;

import akka.actor.ActorRef;
import com.framework.message.ApiRequestForward;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/27.
 */
public interface ServerRequestForwardHandler {

    void onRequest(Internal.EnumLite forwardId, ApiRequestForward request, ActorRef sender);

}
