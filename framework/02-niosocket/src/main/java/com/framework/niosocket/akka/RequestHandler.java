package com.framework.niosocket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.framework.niosocket.RequestWatching;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ChannelOutputHandler;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class RequestHandler extends AbstractActor implements RequestWatching {

    private final int domain;
    protected final ChannelOutputHandler context;

    public RequestHandler(int domain, ChannelOutputHandler context) {
        this.domain = domain;
        this.context = context;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                // 请求事件
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    @Override
    public final void reply(ResponseMessage message) {
        context.writeAndFlush(domain, message);
    }

    public static Props props(Class<? extends RequestHandler> clazz, int domain, ChannelOutputHandler context) {
        return Props.create(clazz, domain, context);
    }

}