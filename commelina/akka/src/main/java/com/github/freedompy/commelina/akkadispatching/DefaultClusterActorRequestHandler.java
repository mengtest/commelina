package com.github.freedompy.commelina.akkadispatching;

import com.github.freedompy.commelina.akkadispatching.cluster.AkkaMultiWorkerSystemContext;
import com.github.freedompy.commelina.akkadispatching.proto.ApiRequest;
import com.github.freedompy.commelina.akkadispatching.cluster.AkkaMultiWorkerSystem;
import com.github.freedompy.commelina.niosocket.ReplyUtils;
import com.github.freedompy.commelina.niosocket.proto.SERVER_CODE;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public abstract class DefaultClusterActorRequestHandler extends DefaultLocalActorRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx) {
        AkkaMultiWorkerSystem clusterSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(getRouterId().getNumber());
        if (clusterSystem == null) {
            ReplyUtils.reply(ctx, SERVER_CODE.RPC_API_NOT_FOUND, getRouterId(), request.getOpcode());
            return;
        }

        ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), clusterSystem.askRouterClusterNode(request));
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

}