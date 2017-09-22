package com.framework.niosocket;

import com.framework.message.ApiRequest;
import com.framework.message.RequestArg;
import com.framework.niosocket.proto.Arg;
import com.framework.niosocket.proto.SERVER_CODE;
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
        RequestHandler handler = handlers.get(request.getApiCode());
        if (handler == null) {
            Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createErrorMessage(SERVER_CODE.RPC_API_NOT_FOUND);
            ctx.writeAndFlush(msg);
            // FIXME: 2017/8/29 返回值未处理
            return;
        }

        final ChannelContextOutputHandler outputHandler = new ChannelContextOutputHandler();
        outputHandler.channelHandlerContext = ctx;

        final RequestArg[] args = new RequestArg[request.getArgsList().size()];
        for (int i = 0; i < request.getArgsList().size(); i++) {
            Arg arg = request.getArgsList().get(i);
            args[i] = new RequestArg(arg.getValue(), RequestArg.DATA_TYPE.valueOf(arg.getDataType().name()));
        }

        handler.onRequest(ApiRequest.newApiRequest(() -> request.getApiMethod(), request.getVersion(), args), outputHandler);
    }

    public void addRequestHandlers(Map<Integer, RequestHandler> handlers) {
        this.handlers.putAll(handlers);
    }

}