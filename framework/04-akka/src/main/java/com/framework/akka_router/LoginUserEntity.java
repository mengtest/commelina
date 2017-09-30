package com.framework.akka_router;

/**
 * Created by @panyao on 2017/9/30.
 */
public class LoginUserEntity {

    private final long userId;

    public LoginUserEntity(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }
}
