package com.framework.akka;

/**
 * Created by @panyao on 2017/8/30.
 */
public interface ActorClientRouterWatching {

    // 请求
    void onRequest(ApiRequestWithActor request);

    // 上线
    default void onOnline(MemberOfflineEvent.MemberOnlineEvent onlineEventWithLogin){

    }

    // 下线
    default void onOffline(MemberOfflineEvent offlineEvent){

    }

}
