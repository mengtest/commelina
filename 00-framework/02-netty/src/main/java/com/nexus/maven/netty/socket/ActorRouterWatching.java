package com.nexus.maven.netty.socket;

import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.MemberOfflineEvent;
import com.nexus.maven.core.message.MemberOnlineEvent;

/**
 * Created by panyao on 2017/8/30.
 */
public interface ActorRouterWatching {


    void onRequest(ApiRequest request);

    default void onOnlineEvent(ActorMemberOnlineEvent onlineEvent) {
        // TODO: 2017/8/29 nothing
    }

    default void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        // TODO: 2017/8/29 nothing
    }

}
