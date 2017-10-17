package com.framework.akka.router;

import com.framework.akka.router.cluster.AkkaMultiWorkerSystemContext;
import com.framework.akka.router.proto.ApiRequest;
import com.framework.akka.router.cluster.AkkaMultiWorkerSystem;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.proto.SERVER_CODE;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author @panyao
 * @date 2017/9/25
 */
public abstract class DefaultClusterActorRequestHandler extends DefaultLocalActorRequestHandler{

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx) {
        AkkaMultiWorkerSystem clusterSystem = AkkaMultiWorkerSystemContext.INSTANCE.getContext(getRouterId());
        if (clusterSystem == null) {
            ReplyUtils.reply(ctx, SERVER_CODE.RPC_API_NOT_FOUND, getRouterId(), request.getOpcode());
            return;
        }

        ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), clusterSystem.askRouterClusterNode(request));
    }

}