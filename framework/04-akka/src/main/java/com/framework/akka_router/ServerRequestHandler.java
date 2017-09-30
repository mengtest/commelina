package com.framework.akka_router;

import akka.actor.ActorRef;
import com.framework.message.ApiRequestForward;

/**
 * Created by @panyao on 2017/9/27.
 */
public interface ServerRequestHandler {

    void onForward(ApiRequestForward request, ActorRef target);

}
