package com.nexus.maven.netty.socket;

import io.socket.netty.proto.SocketNettyProtocol;

/**
 * Created by @panyao on 2017/8/10.
 */
public interface MessageHandler extends MessageVersion, Message {

    PipelineFuture getListener();

    int getOpCode();

    SocketNettyProtocol.BusinessProtocol getBp();

}
