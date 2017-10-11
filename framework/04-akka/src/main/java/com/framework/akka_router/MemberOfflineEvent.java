package com.framework.akka_router;

/**
 * Created by @panyao on 2017/10/11.
 */
public class MemberOfflineEvent {
    private final long logoutUserId;

    public MemberOfflineEvent(long logoutUserId) {
        this.logoutUserId = logoutUserId;
    }

    public long getLogoutUserId() {
        return logoutUserId;
    }

}
