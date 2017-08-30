package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.ApiRouterRequest;
import com.nexus.maven.core.message.MemberOfflineEvent;

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
