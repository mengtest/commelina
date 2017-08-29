package com.nexus.maven.netty.socket;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.MemberOfflineEvent;
import com.nexus.maven.core.message.ResponseMessage;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class ActorWithRequestRouter extends AbstractActor {

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
                .match(ApiRequest.class, this::onRequest)
                //
                .match(ResponseMessage.class, responseMessage -> context.writeAndFlush(domain, responseMessage.getMessage()))
                // 上线事件
                .match(ActorMemberOnlineEvent.class, this::onOnlineEvent)
                // 下线事件
                .match(MemberOfflineEvent.class, this::onOfflineEvent)
                .build();
    }

    protected abstract void onRequest(ApiRequest request);

    protected void onOnlineEvent(ActorMemberOnlineEvent onlineEvent) {

    }

    protected void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        getContext().stop(getSelf());
    }

    public static Props props(int domain, ChannelOutputHandler context) {
        return Props.create(ActorWithRequestRouter.class, domain, context);
    }

}