package com.framework.akka_router;

import java.io.Serializable;

/**
 * Created by @panyao on 2017/10/11.
 */
public class MemberOfflineEvent implements Serializable {
    private final long logoutUserId;

    public MemberOfflineEvent(long logoutUserId) {
        this.logoutUserId = logoutUserId;
    }

    public long getLogoutUserId() {
        return logoutUserId;
    }

}
