package com.nexus.maven.netty.socket;

import com.google.common.base.Preconditions;
import com.nexus.maven.core.message.MessageBus;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class NotifyJsonResponseHandler implements NotifyResponseHandler {

    private final long userId;
    private final MessageBus message;
    private final PipelineNotifyFuture pipelineCallbackFuture;

    private NotifyJsonResponseHandler(long userId, MessageBus messageBus, PipelineNotifyFuture pipelineCallbackFuture) {
        Preconditions.checkArgument(userId > 0);
        this.userId = userId;
        this.message = messageBus;
        this.pipelineCallbackFuture = pipelineCallbackFuture;
    }

    public static NotifyJsonResponseHandler newHandler(long userId, MessageBus messageBus) {
        Preconditions.checkNotNull(messageBus);
        return new NotifyJsonResponseHandler(userId, messageBus, null);
    }

    public static NotifyJsonResponseHandler newHandler(long userId, MessageBus messageBus, PipelineNotifyFuture callableHandler) {
        Preconditions.checkNotNull(messageBus);
        Preconditions.checkNotNull(callableHandler);
        return new NotifyJsonResponseHandler(userId, messageBus, callableHandler);
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
}
