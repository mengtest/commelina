package com.github.freedompy.commelina.web;

/**
 * @author @panyao
 * @date 2017/8/31
 */
public interface SessionHandler {

    String ATTRIBUTE_USER_ID = "userId";
    String ATTRIBUTE_SID = "sid";

    /**
     * 成功 != null
     *
     * @param token
     * @return
     */
    SessionTokenEntity validToken(String token);

    /**
     * 执行登录操作
     *
     * @param userId
     * @return
     */
    SessionTokenEntity doSignIn(long userId);

    /**
     * 初始化匿名用户
     *
     * @return
     */
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
