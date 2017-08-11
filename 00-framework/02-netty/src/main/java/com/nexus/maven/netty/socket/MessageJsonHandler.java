package com.nexus.maven.netty.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.nexus.maven.utils.Generator;
import io.socket.netty.proto.SocketNettyProtocol;

import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class MessageJsonHandler implements MessageHandler {

    private static final Logger LOGGER = Logger.getLogger(MessageJsonHandler.class.getName());

    private static final BusinessMessage EMPTY_RESPONSE_JSON_MESSAGE =
            BusinessMessage.success();

    private final int opCode;
    private final BusinessMessage message;
    private final PipelineFuture pipelineCallbackFuture;

    private MessageJsonHandler(int opCode, BusinessMessage message, PipelineFuture pipelineCallbackFuture) {
        this.opCode = opCode;
        this.message = message;
        this.pipelineCallbackFuture = pipelineCallbackFuture;
    }

    public static MessageJsonHandler newHandler(int opCode) {
        Preconditions.checkArgument(opCode >= 0);
        return new MessageJsonHandler(opCode,  EMPTY_RESPONSE_JSON_MESSAGE, null);
    }

    public static MessageJsonHandler newHandler(int opCode, BusinessMessage message) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(message);
        return new MessageJsonHandler(opCode,  message, null);
    }

    public static MessageJsonHandler newHandler(int opCode, BusinessMessage message, PipelineFuture callableHandler) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(callableHandler);
        return new MessageJsonHandler(opCode, message, callableHandler);
    }

    public static MessageJsonHandler newHandler(int opCode, PipelineFuture callableHandler) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(callableHandler);
        return new MessageJsonHandler(opCode,EMPTY_RESPONSE_JSON_MESSAGE, callableHandler);
    }

    public PipelineFuture getListener() {
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

}
