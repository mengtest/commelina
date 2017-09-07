package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.*;

/**
 * Created by @panyao on 2017/8/29.
 * <p>
 * 接受 客户端请求
 */
public abstract class AbstractReceiveRequestActor extends AbstractActor implements ActorClientRouterWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiLoginRequest.class, this::onRequest)
                .match(ResponseMessage.class, r -> getSender().tell(r, getSelf()))
                .build();
    }

}
