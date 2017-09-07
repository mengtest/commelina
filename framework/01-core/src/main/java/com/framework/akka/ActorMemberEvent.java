package com.framework.akka;

/**
 * Created by @panyao on 2017/9/7.
 */
public interface ActorMemberEvent {

    void onOnlineEvent(MemberOnlineEvent onlineEvent);

    void onOfflineEvent(MemberOfflineEvent offlineEvent);

}
