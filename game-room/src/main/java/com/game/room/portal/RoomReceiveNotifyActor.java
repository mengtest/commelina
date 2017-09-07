package com.game.room.portal;

import akka.actor.Props;
import com.framework.akka.AbstractReceiveNotifyActor;
import com.framework.akka.MemberOfflineEvent;
import com.framework.akka.MemberOnlineEvent;

/**
 * Created by @panyao on 2017/9/7.
 */
public class RoomReceiveNotifyActor extends AbstractReceiveNotifyActor {

    @Override
    public void onOnlineEvent(MemberOnlineEvent onlineEvent) {

    }

    @Override
    public void onOfflineEvent(MemberOfflineEvent offlineEvent) {

    }

    public static Props props() {
        return Props.create(RoomReceiveNotifyActor.class);
    }

}
