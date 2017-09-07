package com.framework.niosocket;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.framework.akka.ActorRequestWatching;
import com.framework.message.ResponseMessage;
import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class ActorRequestHandler extends AbstractActor implements ActorRequestWatching {

    final int domain;
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
                // 响应消息
                .match(ResponseMessage.class, responseMessage -> context.writeAndFlush(domain, responseMessage))
                .build();
    }

    public static Props props(Class<? extends ActorRequestHandler> clazz, int domain, ChannelOutputHandler context) {
        return Props.create(clazz, domain, context);
    }

}