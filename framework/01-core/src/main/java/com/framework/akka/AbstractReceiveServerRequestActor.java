package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;

/**
 * Created by @panyao on 2017/8/29.
 * <p>
 * 接受 客户端请求
 */
public abstract class AbstractReceiveServerRequestActor extends AbstractActor implements ActorRequestWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .match(ResponseMessage.class, r -> getSender().tell(r, getSelf()))
                .build();
    }

}
