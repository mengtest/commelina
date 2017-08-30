package com.framework.coreakka;

import com.framework.core_message.ApiRequestWithActor;
import com.framework.core_message.MemberOfflineEvent;
import com.framework.core_message.MemberOnlineEvent;

/**
 * Created by @panyao on 2017/8/30.
 */
public interface RouterActorWatching {

    // 请求
    boolean onRequest(ApiRequestWithActor request);

    // 上线
    default void onOnline(MemberOnlineEvent onlineEventWithLogin){

    }

    // 下线
    default void onOffline(MemberOfflineEvent offlineEvent){

    }

}
