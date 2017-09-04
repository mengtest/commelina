package com.framework.message;

/**
 * Created by @panyao on 2017/8/30.
 */
public interface RouterActorWatching {

    // 请求
    void onRequest(ApiRequestWithActor request);

    // 上线
    default void onOnline(MemberOnlineEvent onlineEventWithLogin){

    }

    // 下线
    default void onOffline(MemberOfflineEvent offlineEvent){

    }

}
