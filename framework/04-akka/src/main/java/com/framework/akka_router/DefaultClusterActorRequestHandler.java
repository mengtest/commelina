package com.framework.akka_router;

import com.framework.akka_router.cluster.ClusterAskUtils;
import com.framework.message.ApiRequest;
import com.framework.message.MessageBus;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.InvalidParameterException;

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
        Object result = ClusterAskUtils.askRouterClusterNode(getRouterId(), request);
        if (result instanceof MessageBus) {
            ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), ((ResponseMessage) result).getMessage());
        } else {
            throw new InvalidParameterException("Undefined type: " + result.getClass().getName());
        }
    }

}