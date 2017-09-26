package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.*;

/**
 * Created by @panyao on 2017/8/29.
 * <p>
 * 接受 客户端请求
 */
public abstract class AbstractReceiveRequestActor extends AbstractActor implements ActorRemoteProxyWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    @Override
    public final void reply(ResponseMessageDomain message) {
        getSender().tell(message, getSelf());
    }

}