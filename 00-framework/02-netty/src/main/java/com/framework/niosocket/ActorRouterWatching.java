package com.framework.niosocket;


import com.framework.core_message.ApiRouterRequest;
import com.framework.core_message.MemberOfflineEvent;

/**
 * Created by panyao on 2017/8/30.
 */
public interface ActorRouterWatching {

    void onRequest(ApiRouterRequest request);

    default void onOnlineEvent(ActorRouterWatching.MemberOnlineEvent onlineEvent) {
        // TODO: 2017/8/29 nothing
    }

    default void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        // TODO: 2017/8/29 nothing
    }

    class MemberOnlineEvent {

    }

}
