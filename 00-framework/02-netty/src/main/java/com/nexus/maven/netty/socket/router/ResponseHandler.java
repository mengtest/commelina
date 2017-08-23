package com.nexus.maven.netty.socket.router;

import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.netty.socket.PipelineFuture;

/**
 * Created by @panyao on 2017/8/10.
 */
public interface ResponseHandler {

    PipelineFuture getListener();

    MessageBus getMessage();

    int getDomain();

}
