package com.framework.akka_router;

import com.framework.akka_router.local.AkkaLocalWorkerSystem;
import com.framework.core.MessageBus;
import com.framework.niosocket.message.ResponseMessage;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.RequestHandler;
import com.framework.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class DefaultLocalActorRequestHandler implements RequestHandler, Router {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final void onRequest(SocketASK ask, ChannelHandlerContext ctx) {
        final ApiRequest.Builder newRequestBuilder = ApiRequest.newBuilder()
                .setOpcode(ask.getOpcode())
                .setVersion(ask.getVersion())
                .addAllArgs(ask.getArgsList());

        if (beforeHook(ask, newRequestBuilder, ctx)) {
            afterHook(newRequestBuilder.build(), ctx);
        }
    }

    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {
        return true;
    }

    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx) {
        Object result = AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(request);
        if (result instanceof MessageBus) {
            ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), ((ResponseMessage) result).getMessage());
        } else if (result instanceof LoginUserEntity) {
            ContextAdapter.userLogin(ctx.channel().id(), ((LoginUserEntity) result).getUserId());
            ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), ((LoginUserEntity) result).getMessageBus());
        } else {
            throw new InvalidParameterException("Undefined type: " + result.getClass().getName());
        }
    }

    protected Logger getLogger() {
        return logger;
    }
}