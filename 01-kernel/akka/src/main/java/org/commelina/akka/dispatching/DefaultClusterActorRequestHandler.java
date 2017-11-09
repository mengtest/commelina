package org.commelina.akka.dispatching;

import org.commelina.akka.dispatching.cluster.AkkaMultiWorkerSystemContext;
import org.commelina.niosocket.ReplyUtils;
import org.commelina.akka.dispatching.proto.ApiRequest;
import org.commelina.akka.dispatching.cluster.AkkaMultiWorkerSystem;
import org.commelina.niosocket.proto.SERVER_CODE;
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