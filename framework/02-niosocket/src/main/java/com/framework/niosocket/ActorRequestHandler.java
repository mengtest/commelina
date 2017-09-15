package com.framework.niosocket;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.framework.akka.ActorRequestWatching;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class ActorRequestHandler extends AbstractActor implements ActorRequestWatching {

    private final int domain;
    protected final ChannelOutputHandler context;

    public ActorRequestHandler(int domain, ChannelOutputHandler context) {
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

    public static Props props(Class<? extends ActorRequestHandler> clazz, int domain, ChannelOutputHandler context) {
        return Props.create(clazz, domain, context);
    }

}