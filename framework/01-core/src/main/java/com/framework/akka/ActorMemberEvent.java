package com.framework.akka;

/**
 * Created by @panyao on 2017/9/7.
 */
 interface ActorMemberEvent {

    void onEvent(MemberOnlineEvent onlineEvent);

    void onEvent(MemberOfflineEvent offlineEvent);

}
