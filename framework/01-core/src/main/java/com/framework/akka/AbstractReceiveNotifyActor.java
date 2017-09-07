package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.*;

/**
 * Created by @panyao on 2017/8/29.
 * <p>
 * 接受通知请求
 */
public abstract class AbstractReceiveNotifyActor extends AbstractActor implements ActorMemberEvent {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 用户离线事件 直接发送到远程
                .match(MemberOfflineEvent.class, this::onOfflineEvent)
                // 用户上线事件，直接发送到远程
                .match(MemberOnlineEvent.class, this::onOnlineEvent)

                .match(NotifyMessage.class, n -> getSender().tell(n, getSelf()))
                .match(BroadcastMessage.class, b -> getSender().tell(b, getSelf()))
                .match(WorldMessage.class, w -> getSender().tell(w, getSelf()))
//                .match(MemberOfflineEvent.MemberOnlineEvent.class, this::onOnline)
//                .match(MemberOfflineEvent.class, this::onOffline)
                .build();
    }

}
