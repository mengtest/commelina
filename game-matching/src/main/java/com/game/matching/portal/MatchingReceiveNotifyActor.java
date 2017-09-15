package com.game.matching.portal;

import akka.actor.Props;
import com.framework.akka.AbstractForwardRemoteReceiveActor;
import com.framework.akka.MemberOfflineEvent;
import com.framework.akka.MemberOnlineEvent;
import com.framework.message.ApiRequestForward;

/**
 * Created by @panyao on 2017/9/7.
 */
public class MatchingReceiveNotifyActor extends AbstractForwardRemoteReceiveActor {

    public static Props props() {
        return Props.create(MatchingReceiveNotifyActor.class);
    }

    @Override
    public void onEvent(MemberOnlineEvent onlineEvent) {

    }

    @Override
    public void onEvent(MemberOfflineEvent offlineEvent) {

    }

    @Override
    public void onForwardEvent(ApiRequestForward forward) {

    }
}
