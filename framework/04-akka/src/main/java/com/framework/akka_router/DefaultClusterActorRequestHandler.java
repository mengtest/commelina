package com.framework.akka_router;

import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import com.framework.akka_router.cluster.ClusterAskUtils;
import com.framework.akka_router.local.AkkaLocalWorkerSystem;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ProtoBuffMap;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.Future;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class DefaultClusterActorRequestHandler implements RequestHandler, Router {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final void onRequest(ApiRequest request, ChannelHandlerContext ctx) {
        if (beforeHook(request, ctx)) {
            afterHook(request, ctx);
        }
    }

    protected boolean beforeHook(ApiRequest request, ChannelHandlerContext ctx) {
        return true;
    }

    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx) {
        Future<Object> future = ClusterAskUtils.askRouterClusterNode(getRouterId(), request);
        // actor 处理成功
        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), ((ResponseMessage) result).getMessage());
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