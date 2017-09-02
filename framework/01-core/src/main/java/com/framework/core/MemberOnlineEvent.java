package com.framework.core;

/**
 * Created by @panyao on 2017/8/29.
 */
public class MemberOnlineEvent {

    private final long userId;

    public MemberOnlineEvent(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

}
