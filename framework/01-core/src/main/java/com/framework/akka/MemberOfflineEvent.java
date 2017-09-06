package com.framework.akka;

/**
 * Created by @panyao on 2017/8/29.
 */
public class MemberOfflineEvent {

    private final long userId;

    public MemberOfflineEvent(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    /**
     * Created by @panyao on 2017/8/29.
     */
    public static class MemberOnlineEvent {

        private final long userId;

        public MemberOnlineEvent(long userId) {
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }

    }
}
