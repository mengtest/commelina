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

    TokenEntity initAnonymous();

    class SessionTokenEntity {
        long userId;
        TokenEntity tokenEntity;

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public void setTokenEntity(TokenEntity tokenEntity) {
            this.tokenEntity = tokenEntity;
        }
    }

    class TokenEntity {
        long sid;
        String newToken;

        public void setSid(long sid) {
            this.sid = sid;
        }

        public void setNewToken(String newToken) {
            this.newToken = newToken;
        }

    }

}
