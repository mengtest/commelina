package com.framework.niosocket.akka;

import io.netty.channel.ChannelId;

/**
 * Created by @panyao on 2017/9/7.
 */
@Deprecated
public interface ActorSocketMemberEvent {

    default void onOnlineEvent(SocketMemberOnlineEvent onlineEvent) {
        // TODO: 2017/9/7  
    }

    default void onOfflineEvent(SocketMemberOfflineEvent offlineEvent) {
        // TODO: 2017/9/7  
    }

    // 长连接用户上线通知
    class SocketMemberOnlineEvent {
        ChannelId channelId;

        public ChannelId getChannelId() {
            return channelId;
        }

    }

    // 长连接用户下线通知
    class SocketMemberOfflineEvent {
        ChannelId channelId;
        long userId;

        public long getUserId() {
            return userId;
        }

        public ChannelId getChannelId() {
            return channelId;
        }
    }

}
