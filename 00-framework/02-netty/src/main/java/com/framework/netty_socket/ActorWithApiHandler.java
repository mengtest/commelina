package com.framework.netty_socket;

import akka.actor.Props;

/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorWithApiHandler {

    Props getProps(ChannelOutputHandler outputHandler);

}
