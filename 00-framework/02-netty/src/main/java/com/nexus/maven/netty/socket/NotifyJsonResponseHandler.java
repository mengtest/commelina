package com.nexus.maven.netty.socket;

import com.google.common.base.Preconditions;
import com.nexus.maven.core.message.MessageBus;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class NotifyJsonResponseHandler implements NotifyResponseHandler {

    private final int domain;
    private final long userId;
    private final MessageBus message;
    private final PipelineNotifyFuture pipelineCallbackFuture;

    private NotifyJsonResponseHandler(int domain, long userId, MessageBus messageBus, PipelineNotifyFuture pipelineCallbackFuture) {
        Preconditions.checkArgument(domain >= 0);
        Preconditions.checkArgument(userId > 0);
        this.domain = domain;
        this.userId = userId;
        this.message = messageBus;
        this.pipelineCallbackFuture = pipelineCallbackFuture;
    }

    public static NotifyJsonResponseHandler newHandler(int domain, long userId, MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus);
        return new NotifyJsonResponseHandler(domain, userId, messageBus, null);
    }

    public static NotifyJsonResponseHandler newHandler(int domain, long userId, MessageBus messageBus, PipelineNotifyFuture callableHandler) {
        Preconditions.checkNotNull(messageBus);
        Preconditions.checkNotNull(callableHandler);
        return new NotifyJsonResponseHandler(domain, userId, messageBus, callableHandler);
    }

    public PipelineNotifyFuture getListener() {
        return this.pipelineCallbackFuture;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public MessageBus getMessage() {
        return this.message;
    }

    @Override
    public int getDomain() {
        return this.domain;
    }
}
