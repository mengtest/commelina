package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.*;

/**
 * Created by @panyao on 2017/8/29.
 *
 * 接受通知请求
 *
 */
public abstract class AbstractReceiveNotifyActor extends AbstractActor implements ActorClientRouterWatching {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiLoginRequest.class, this::onRequest)
                .match(ResponseMessage.class, r -> getSender().tell(r, getSelf()))
                .match(NotifyMessage.class, n -> getSender().tell(n, getSelf()))
                .match(BroadcastMessage.class, b -> getSender().tell(b, getSelf()))
                .match(WorldMessage.class, w -> getSender().tell(w, getSelf()))
//                .match(MemberOfflineEvent.MemberOnlineEvent.class, this::onOnline)
//                .match(MemberOfflineEvent.class, this::onOffline)
                .build();
    }

}
