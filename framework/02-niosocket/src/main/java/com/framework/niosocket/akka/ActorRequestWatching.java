package com.framework.niosocket.akka;

import akka.actor.Props;
import com.framework.niosocket.ChannelOutputHandler;
import com.framework.niosocket.RequestWatching;

/**
 * Created by @panyao on 2017/8/25.
 */
public interface ActorRequestWatching extends RequestWatching {

    Props getProps(ChannelOutputHandler outputHandler);

}
