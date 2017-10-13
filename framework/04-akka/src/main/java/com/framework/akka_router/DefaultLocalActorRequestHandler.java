package com.framework.akka_router;

import com.framework.akka_router.local.AkkaLocalWorkerSystem;
import com.framework.message.ApiRequest;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.RequestHandler;
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
    public final void onRequest(ApiRequest request, ChannelHandlerContext ctx) {
        if (beforeHook(request, ctx)) {
            loginAfterHook(request, ctx);
        }
    }

    protected boolean beforeHook(ApiRequest request, ChannelHandlerContext ctx) {
        return true;
    }

    protected void loginAfterHook(ApiRequest request, ChannelHandlerContext ctx) {
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