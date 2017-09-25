package com.framework.akka;

import akka.actor.AbstractActor;
import com.framework.message.ApiRequestForward;
import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;

/**
 * Created by @panyao on 2017/8/29.
 * <p>
 * 接受通知请求
 */
public abstract class AbstractForwardRemoteReceiveActor extends AbstractActor implements ActorRemoteForwardReceiveHandler {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 用户离线事件 直接发送到远程
                .match(MemberOfflineEvent.class, this::onEvent)
                // 用户上线事件，直接发送到远程
                .match(MemberOnlineEvent.class, this::onEvent)
                // 接受重定向过来的 server request
                .match(ApiRequestForward.class, this::onForwardEvent)
                .build();
    }

    @Override
    public final void reply(NotifyMessage message) {
        getSender().tell(message, getSelf());
    }

    @Override
    public final void reply(BroadcastMessage message) {
        getSender().tell(message, getSelf());
    }

    @Override
    public final void reply(WorldMessage message) {
        getSender().tell(message, getSelf());
    }

}
