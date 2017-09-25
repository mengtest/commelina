package com.framework.nio_akka;

import akka.actor.Props;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/8/25.
 */
@Deprecated
public interface ActorRequestHandler extends com.framework.niosocket.RequestHandler {

   default Props getProps(ChannelHandlerContext outputHandler){

       return  null;
   }

}
