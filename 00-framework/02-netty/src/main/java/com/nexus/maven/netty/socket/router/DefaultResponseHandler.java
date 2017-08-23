package com.nexus.maven.netty.socket.router;

import com.google.common.base.Preconditions;
import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.netty.socket.PipelineFuture;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class DefaultResponseHandler implements ResponseHandler {

    private final int domain;
    private final MessageBus message;
    private final PipelineFuture pipelineCallbackFuture;

    private DefaultResponseHandler(int domain, MessageBus message, PipelineFuture pipelineCallbackFuture) {
        this.domain = domain;
        this.message = message;
        this.pipelineCallbackFuture = pipelineCallbackFuture;
    }

    public static DefaultResponseHandler newHandler(int domain, MessageBus message, PipelineFuture pipelineCallbackFuture) {
        Preconditions.checkArgument(domain >= 0);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(pipelineCallbackFuture);
        return new DefaultResponseHandler(domain, message, pipelineCallbackFuture);
    }

    public static DefaultResponseHandler newHandler(int domain, MessageBus message) {
        Preconditions.checkArgument(domain >= 0);
        Preconditions.checkNotNull(message);
        return new DefaultResponseHandler(domain, message, null);
    }

    public PipelineFuture getListener() {
        return this.pipelineCallbackFuture;
    }

    public MessageBus getMessage() {
        return this.message;
    }

    @Override
    public int getDomain() {
        return this.domain;
    }
}
