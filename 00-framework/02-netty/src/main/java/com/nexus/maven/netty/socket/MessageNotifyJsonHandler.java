package com.nexus.maven.netty.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.nexus.maven.utils.Generator;
import io.socket.netty.proto.SocketNettyProtocol;

import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class MessageNotifyJsonHandler implements MessageNotifyHandler {

    private static final Logger LOGGER = Logger.getLogger(MessageNotifyJsonHandler.class.getName());

    private static final BusinessMessage EMPTY_RESPONSE_JSON_MESSAGE =
            BusinessMessage.success();

    private final long userId;
    private final int opCode;
    private final String version;
    private final BusinessMessage message;
    private final PipelineNotifyFuture pipelineCallbackFuture;

    private MessageNotifyJsonHandler(long userId, int opCode, String version, BusinessMessage message, PipelineNotifyFuture pipelineCallbackFuture) {
        Preconditions.checkArgument(userId > 0);
        this.userId = userId;
        this.opCode = opCode;
        this.version = version;
        this.message = message;
        this.pipelineCallbackFuture = pipelineCallbackFuture;
    }

    public static MessageNotifyJsonHandler newHandler(long userId, int opCode) {
        Preconditions.checkArgument(opCode >= 0);
        return new MessageNotifyJsonHandler(userId, opCode, MessageVersion.FIRST_VERSION, EMPTY_RESPONSE_JSON_MESSAGE, null);
    }

    public static MessageNotifyJsonHandler newHandler(long userId, int opCode, String version, BusinessMessage message) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(version);
        Preconditions.checkNotNull(message);
        return new MessageNotifyJsonHandler(userId, opCode, version, message, null);
    }

    public static MessageNotifyJsonHandler newHandler(long userId, int opCode, BusinessMessage message) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(message);
        return new MessageNotifyJsonHandler(userId, opCode, MessageVersion.FIRST_VERSION, message, null);
    }

    public static MessageNotifyJsonHandler newHandler(long userId, int opCode, String version, BusinessMessage message, PipelineNotifyFuture callableHandler) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(version);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(callableHandler);
        return new MessageNotifyJsonHandler(userId, opCode, version, message, callableHandler);
    }

    public static MessageNotifyJsonHandler newHandler(long userId, int opCode, String version, PipelineNotifyFuture callableHandler) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(version);
        Preconditions.checkNotNull(callableHandler);
        return new MessageNotifyJsonHandler(userId, opCode, version, EMPTY_RESPONSE_JSON_MESSAGE, callableHandler);
    }

    public PipelineNotifyFuture getListener() {
        return this.pipelineCallbackFuture;
    }

    public byte[] getBytes() {
        try {
            return Generator.getJsonHolder().writeValueAsBytes(this.message);
        } catch (JsonProcessingException e) {
            LOGGER.info(e.getMessage());
            return null;
        }
    }

    public int getOpCode() {
        return opCode;
    }

    public SocketNettyProtocol.BusinessProtocol getBp() {
        return SocketNettyProtocol.BusinessProtocol.JSON;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public String getVersion() {
        return this.version;
    }
}
