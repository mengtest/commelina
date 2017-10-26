package com.github.freedompy.commelina.niosocket;

import com.github.freedompy.commelina.niosocket.proto.SERVER_CODE;
import com.github.freedompy.commelina.niosocket.proto.SocketASK;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;

import java.util.Map;

/**
 * 路由上下文实现
 *
 * @author @panyao
 * @date 2017/9/22
 */
class RouterContextHandlerImpl implements RouterContextHandler {

    /**
     * apiPathCode -> ActorRequestHandler
     */
    private final Map<Integer, RequestHandler> handlers = Maps.newLinkedHashMap();

    @Override
    public void onRequest(ChannelHandlerContext ctx, SocketASK request) {
        RequestHandler handler = handlers.get(request.getForward());
        if (handler == null) {
            ReplyUtils.reply(ctx, ProtoBuffMap.createMessage(SERVER_CODE.RPC_API_NOT_FOUND, request.getForward(), request.getOpcode()));
            return;
        }

        // 依然是在 accept 线程内
        handler.onRequest(request, ctx);
    }

    void addRequestHandlers(Map<Integer, RequestHandler> handlers) {
        this.handlers.putAll(handlers);
    }

}