package com.framework.netty_socket;

import com.framework.core_message.ApiRouterRequest;
import com.framework.core_message.MemberOfflineEvent;

/**
 * Created by panyao on 2017/8/30.
 */
public interface ActorRouterWatching {

    boolean onRequest(ApiRouterRequest request);

    default void onOnlineEvent(ActorMemberOnlineEvent onlineEvent) {
        // TODO: 2017/8/29 nothing
    }

    default void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        // TODO: 2017/8/29 nothing
    }

}
