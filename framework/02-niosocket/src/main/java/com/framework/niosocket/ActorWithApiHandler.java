package com.framework.niosocket;

import akka.actor.Props;
import com.framework.niosocket.ChannelOutputHandler;

/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorWithApiHandler {

    Props getProps(ChannelOutputHandler outputHandler);

}
