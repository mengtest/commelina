package com.framework.webmvc;

/**
 * Created by @panyao on 2017/8/31.
 */
public interface SessionHandler {

    ValidTokenEntity validToken(String token);

    String doSignIn(long userId);

    String iniAnonymous();

    class ValidTokenEntity {
        long userId;
        String newToken;
    }
}
