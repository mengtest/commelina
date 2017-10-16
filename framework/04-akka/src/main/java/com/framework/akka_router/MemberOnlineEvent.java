package com.framework.akka_router;

import java.io.Serializable;

/**
 * Created by @panyao on 2017/10/11.
 */
public class MemberOnlineEvent implements Serializable {
    private final long logoutUserId;

    public MemberOnlineEvent(long logoutUserId) {
        this.logoutUserId = logoutUserId;
    }

    public long getLogoutUserId() {
        return logoutUserId;
    }

}
