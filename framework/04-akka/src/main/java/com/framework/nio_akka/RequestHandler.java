package com.framework.nio_akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ReplyUtils;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/8/29.
 */
@Deprecated
public abstract class RequestHandler extends AbstractActor {

    private final int domain;
    protected final ChannelHandlerContext context;

    public RequestHandler(int domain, ChannelHandlerContext context) {
        this.domain = domain;
        this.context = context;
    }

    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                // 请求事件
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    public abstract void onRequest(ApiRequest request);

    public final void reply(ResponseMessage message) {
        ReplyUtils.reply(context, () -> domain, message);
    }

    public static Props props(Class<? extends RequestHandler> clazz, int domain, ChannelHandlerContext context) {
        return Props.create(clazz, domain, context);
    }

}