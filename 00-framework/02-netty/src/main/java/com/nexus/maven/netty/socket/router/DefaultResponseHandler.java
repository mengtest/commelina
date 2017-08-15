package com.nexus.maven.netty.socket.router;

import com.google.common.base.Preconditions;
import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.netty.socket.PipelineFuture;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class DefaultResponseHandler implements ResponseHandler {

    private final PipelineFuture pipelineCallbackFuture;
    private final MessageBus message;

    private DefaultResponseHandler(MessageBus message, PipelineFuture pipelineCallbackFuture) {
        this.pipelineCallbackFuture = pipelineCallbackFuture;
        this.message = message;
    }

    public static DefaultResponseHandler newHandler(MessageBus message, PipelineFuture pipelineCallbackFuture) {
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(pipelineCallbackFuture);
        return new DefaultResponseHandler(message, pipelineCallbackFuture);
    }

    public static DefaultResponseHandler newHandler(MessageBus message) {
        Preconditions.checkNotNull(message);
        return new DefaultResponseHandler(message, null);
    }

    public PipelineFuture getListener() {
        return this.pipelineCallbackFuture;
    }

    public MessageBus getMessage() {
        return this.message;
    }

}
