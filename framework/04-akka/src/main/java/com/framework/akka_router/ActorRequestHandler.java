package com.framework.akka_router;

import akka.actor.Props;
import com.framework.niosocket.RequestHandler;

/**
 * Created by @panyao on 2017/9/25.
 */
public interface ActorRequestHandler extends RequestHandler, RouterId {

    Props getProps();

}
