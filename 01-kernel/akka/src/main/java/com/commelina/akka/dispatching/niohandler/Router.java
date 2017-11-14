package com.commelina.akka.dispatching.niohandler;

import com.commelina.niosocket.RequestRouterHandler;
import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author panyao
 * @date 2017/11/14
 */
public class Router implements RequestRouterHandler {

    @Override
    public void onRequest(ChannelHandlerContext ctx, SocketASK request) {

    }

}
