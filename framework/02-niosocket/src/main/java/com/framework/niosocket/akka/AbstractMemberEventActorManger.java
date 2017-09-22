package com.framework.niosocket.akka;

import akka.actor.ActorSystem;
import com.framework.akka.MemberOfflineEvent;
import com.framework.akka.MemberOnlineEvent;
import com.framework.niosocket.ContextAdapter;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/9/7.
 */
@Deprecated
public abstract class AbstractMemberEventActorManger implements ActorSocketMemberEvent {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected final ActorSystem system = ActorSystem.create("akkaNotifyContext", ConfigFactory.load(("akkanotify")));

    @Override
    public final void onOnlineEvent(SocketMemberOnlineEvent onlineEvent) {
        final long userId =  ContextAdapter.getLoginUserId(onlineEvent.getChannelId());
        if (userId <= 0) {
            logger.info("channelId {}, ignore.", onlineEvent.getChannelId());
            return;
        }

        MemberOnlineEvent event = new MemberOnlineEvent();
        event.setUserId(userId);
        onlineEvent(event);
    }

    @Override
    public final void onOfflineEvent(SocketMemberOfflineEvent offlineEvent) {
        if (offlineEvent.getUserId() <= 0) {
            logger.info("channelId {}, ignore.", offlineEvent.getChannelId());
            return;
        }
        // 离线事件
        final long userId = offlineEvent.getUserId();
        MemberOfflineEvent event = new MemberOfflineEvent();
        event.setUserId(userId);
        offlineEvent(event);
    }

    protected abstract void onlineEvent(MemberOnlineEvent event);

    protected abstract void offlineEvent(MemberOfflineEvent event);

}
