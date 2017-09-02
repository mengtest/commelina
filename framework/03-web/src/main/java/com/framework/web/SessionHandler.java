package com.framework.web;

/**
 * Created by @panyao on 2017/8/31.
 */
public interface SessionHandler {

    ValidTokenEntity validToken(String token);

    String doSignIn(long userId);

    String initAnonymous();

    class ValidTokenEntity {
        long userId;
        String newToken;

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setNewToken(String newToken) {
            this.newToken = newToken;
        }
    }
}
