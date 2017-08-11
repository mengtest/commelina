package com.nexus.maven.netty.router;

import io.socket.netty.proto.SocketNettyProtocol;

/**
 * Created by @panyao on 2017/8/10.
 */
public interface ResponseHandler extends Version {

    PipelineFuture getListener();

    byte[] getBytes();

    int getOpCode();

    SocketNettyProtocol.BusinessProtocol getBp();

}
