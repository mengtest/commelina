package com.framework.niosocket;

/**
 * Created by @panyao on 2017/9/7.
 */
public interface ActorSocketMemberEvent {

    default void onOnlineEvent(SocketMemberOnlineEvent onlineEvent) {
        // TODO: 2017/9/7  
    }

    default void onOfflineEvent(SocketMemberOfflineEvent offlineEvent) {
        // TODO: 2017/9/7  
    }

    // 长连接用户上线通知
    class SocketMemberOnlineEvent {

    }

    // 长连接用户下线通知
    class SocketMemberOfflineEvent {
        long userId;
    }
}
