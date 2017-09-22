package com.framework.niosocket.akka;

import akka.actor.Props;
import com.framework.niosocket.ChannelContextOutputHandler;
import com.framework.niosocket.RequestHandler;

/**
 * Created by @panyao on 2017/8/25.
 */
@Deprecated
public interface ActorRequestHandler extends RequestHandler {

    Props getProps(ChannelContextOutputHandler outputHandler);

}
