package com.framework.niosocket;

import com.framework.message.ApiRequest;
import com.framework.message.RequestArg;
import com.framework.niosocket.proto.Arg;
import com.framework.niosocket.proto.SocketASK;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * Created by @panyao on 2017/9/22.
 */
class RouterContextHandlerImpl implements RouterContextHandler {

    /**
     * apiPathCode -> ActorRequestHandler
     */
    private final Map<Integer, RequestHandler> handlers = Maps.newLinkedHashMap();

    public void onRequest(ChannelHandlerContext ctx, SocketASK request) {
        RequestHandler handler = handlers.get(request.getForward());
        if (handler == null) {
            ReplyUtils.reply(ctx, ProtoBuffMap.RPC_API_NOT_FOUND);
            return;
        }

        final RequestArg[] args = new RequestArg[request.getArgsList().size()];
        for (int i = 0; i < request.getArgsList().size(); i++) {
            Arg arg = request.getArgsList().get(i);
            args[i] = new RequestArg(arg.getValue(), RequestArg.DATA_TYPE.valueOf(arg.getDataType().name()));
        }

        // 依然是在 accept 线程内
        handler.onRequest(ApiRequest.newRequest(() -> request.getOpcode(), request.getVersion(), args), ctx);
    }

    void addRequestHandlers(Map<Integer, RequestHandler> handlers) {
        this.handlers.putAll(handlers);
    }

}