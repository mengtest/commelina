package com.nexus.maven.netty.router;

/**
 * Created by @panyao on 2017/8/10.
 */
public interface ResponseHandler {

    PipelineFuture getListener();

    byte[] getBytes();

    int getOpCode();

}
