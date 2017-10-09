package com.framework.akka_router;

import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import com.framework.akka_router.local.AkkaLocalWorkerSystem;
import com.framework.message.ApiRequest;
import com.framework.message.MessageBus;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.ProtoBuffMap;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;

import java.security.InvalidParameterException;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class DefaultLocalActorRequestHandler implements RequestHandler, Router {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

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
        Future<Object> future = AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(request.getOpcode(), request);
        // actor 处理成功
        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                if (result instanceof MessageBus) {
                    ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), (MessageBus) result);
                } else if (result instanceof LoginUserEntity) {
                    ContextAdapter.userLogin(ctx.channel().id(), ((LoginUserEntity) result).getUserId());
                } else {
                    throw new InvalidParameterException("Undefined type: " + result.getClass().getName());
                }
            }
        }, AkkaLocalWorkerSystem.INSTANCE.getSystem().dispatcher());

        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable failure) throws Throwable {
                ReplyUtils.reply(ctx, ProtoBuffMap.SERVER_ERROR);
                logger.error("actor return error.{}", failure);
            }
        }, AkkaLocalWorkerSystem.INSTANCE.getSystem().dispatcher());

    }

}