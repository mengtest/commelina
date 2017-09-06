package com.framework.niosocket;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.framework.akka.MemberOfflineEvent;
import com.framework.message.ResponseMessage;
import com.framework.message.ApiRouterRequest;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class ActorWithRequestRouter extends AbstractActor implements ActorRouterWatching {

    final int domain;
    protected final ChannelOutputHandler context;

    public ActorWithRequestRouter(int domain, ChannelOutputHandler context) {
        this.domain = domain;
        this.context = context;
    }

    @Override
    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                // 请求事件
                .match(ApiRouterRequest.class, (r) -> {
                    this.onRequest(r);
                })
                // 效应消息
                .match(ResponseMessage.class, responseMessage -> context.writeAndFlush(domain, responseMessage))
                // 上线事件
                .match(MemberOnlineEvent.class, this::onOnlineEvent)
                // 下线事件
                .match(MemberOfflineEvent.class, this::onOfflineEvent)
                .build();
    }

    public void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        getContext().stop(getSelf());
    }

    public static Props props(Class<? extends ActorWithRequestRouter> clazz, int domain, ChannelOutputHandler context) {
        return Props.create(clazz, domain, context);
    }

}