package com.framework.akka_router;

import akka.actor.ActorRef;

/**
 * Created by @panyao on 2017/9/27.
 */
public interface ServerRequestHandler {

    void onForward(ApiRequestForwardEntity request, ActorRef target);

}
