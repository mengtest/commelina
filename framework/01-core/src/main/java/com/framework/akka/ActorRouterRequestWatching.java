package com.framework.akka;

/**
 * Created by panyao on 2017/8/30.
 */
public interface ActorRouterWatching extends ActorWatching {

    default void onOnlineEvent(ActorRouterWatching.MemberOnlineEvent onlineEvent) {
        // TODO: 2017/8/29 nothing
    }

    default void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        // TODO: 2017/8/29 nothing
    }

    class MemberOnlineEvent {

    }

}
