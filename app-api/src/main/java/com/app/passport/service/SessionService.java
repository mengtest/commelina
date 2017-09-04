package com.app.passport.service;

/**
 * Created by @panyao on 2017/9/1.
 */
public interface SessionService {

    long createNewToken(long userId);

    long validToken(long sid);

    void remove(long sid);

}
