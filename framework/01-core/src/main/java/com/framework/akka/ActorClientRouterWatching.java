package com.framework.akka;

import com.framework.message.ApiLoginRequest;

/**
 * Created by @panyao on 2017/8/30.
 */
public interface ActorClientRouterWatching {

    // 请求
    void onRequest(ApiLoginRequest request);

//    // 上线
//    default void onOnline(MemberOfflineEvent.MemberOnlineEvent onlineEventWithLogin){
//
//    }
//
//    // 下线
//    default void onOffline(MemberOfflineEvent offlineEvent){
//
//    }

}
