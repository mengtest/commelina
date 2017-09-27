package com.framework.akka_router;

import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
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
public abstract class DefaultClusterActorRequestHandler implements RequestHandler, Router, Rewrite {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onRequest(ApiRequest request, ChannelHandlerContext ctx) {
        afterHook(request, ctx, beforeHook(request, ctx));
    }

    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx, ClusterRouterJoinEntity message) {
        // 转发到业务 actor 上去
        Future<Object> future = AkkaWorkerSystem.Holder.WORKER.routerClusterNodeAsk(message);

        // actor 处理成功
        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(),
                        ResponseMessage.newMessage(((RouterResponseEntity) result).getMessage()));
            }
        }, AkkaWorkerSystem.Holder.WORKER.getSystem().dispatcher());

        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable failure) throws Throwable {
                ReplyUtils.reply(ctx, ProtoBuffMap.SERVER_ERROR);
                logger.error("Actor return error.{}", failure);
            }
        }, AkkaWorkerSystem.Holder.WORKER.getSystem().dispatcher());
    }

    protected ClusterRouterJoinEntity beforeHook(ApiRequest request, ChannelHandlerContext ctx) {
        return new ClusterRouterJoinEntity(this.getRouterId(), (byte) 0, request);
    }

}