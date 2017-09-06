package com.framework.web;

/**
 * Created by @panyao on 2017/8/31.
 */
public interface SessionHandler {

    String ATTRIBUTE_USER_ID = "userId";
    String ATTRIBUTE_SID = "sid";

    // 成功 != null
    SessionTokenEntity validToken(String token);

    SessionTokenEntity doSignIn(long userId);

    NewTokenEntity initAnonymous();

    class SessionTokenEntity {
        long userId;
        NewTokenEntity newTokenEntity;

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setNewTokenEntity(NewTokenEntity newTokenEntity) {
            this.newTokenEntity = newTokenEntity;
        }
    }

    class NewTokenEntity {
        long sid;
        String newToken;

        public void setSid(long sid) {
            this.sid = sid;
        }

        public void setNewToken(String newToken) {
            this.newToken = newToken;
        }

    }

    SessionTokenEntity ANONYMOUS = new SessionTokenEntity();

}
