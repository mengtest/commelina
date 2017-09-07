package com.framework.akka;

/**
 * Created by @panyao on 2017/9/7.
 */
public interface ActorMemberEvent {

    default void onOnlineEvent(MemberOnlineEvent onlineEvent) {
        // TODO: 2017/9/7
    }

    default void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        // TODO: 2017/9/7
    }

}
