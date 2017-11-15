package com.commelina.akka;

import com.commelina.akka.dispatching.proto.ApiRequest;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author @panyao
 * @date 2017/9/25
 */
@Deprecated
public abstract class DefaultClusterActorRequestHandler extends DefaultLocalActorRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx) {
//        AkkaMultiWorkerSystem clusterSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(getRouterId().getNumber());
//        if (clusterSystem == null) {
//            ReplyUtils.reply(ctx, SERVER_CODE.RPC_API_NOT_FOUND, getRouterId(), request.getOpcode());
//            return;
//        }
//
//        ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), clusterSystem.askActor(request));
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}