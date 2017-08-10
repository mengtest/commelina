package com.nexus.maven.netty.router;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Preconditions;
import com.nexus.maven.utils.Generator;

import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/10.
 */
public final class ResJsonHandler implements ResponseHandler {

    private static final Logger LOGGER = Logger.getLogger(DefaultRpcWithProtoBuff.class.getName());
    private static final ResJsonMessage EMPTY_RESPONSE_JSON_MESSAGE =
            ResJsonMessage.success(null);

    private final int opCode;
    private final ResJsonMessage message;
    private final PipelineFuture pipelineCallbackFuture;

    private ResJsonHandler(int opCode, ResJsonMessage message, PipelineFuture pipelineCallbackFuture) {
        this.opCode = opCode;
        this.message = message;
        this.pipelineCallbackFuture = pipelineCallbackFuture;
    }

    public static ResJsonHandler newHandler(int opCode) {
        Preconditions.checkArgument(opCode >= 0);
        return new ResJsonHandler(opCode, EMPTY_RESPONSE_JSON_MESSAGE, null);
    }

    public static ResJsonHandler newHandler(int opCode, ResJsonMessage message) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(message);
        return new ResJsonHandler(opCode, message, null);
    }

    public static ResJsonHandler newHandler(int opCode, PipelineFuture callableHandler) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(callableHandler);
        return new ResJsonHandler(opCode, EMPTY_RESPONSE_JSON_MESSAGE, callableHandler);
    }

    public static ResJsonHandler newHandler(int opCode, ResJsonMessage message, PipelineFuture callableHandler) {
        Preconditions.checkArgument(opCode >= 0);
        Preconditions.checkNotNull(message);
        Preconditions.checkNotNull(callableHandler);
        return new ResJsonHandler(opCode, message, callableHandler);
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

}
