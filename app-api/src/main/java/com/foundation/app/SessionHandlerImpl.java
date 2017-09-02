package com.foundation.app;

import com.foundation.app.passport.service.SessionService;
import com.framework.web.SessionHandler;

import java.util.UUID;

/**
 * Created by @panyao on 2017/9/1.
 */
public class SessionHandlerImpl implements SessionHandler {

//    @Resource
    private SessionService sessionService;

    @Override
    public ValidTokenEntity validToken(String token) {

        return null;
    }

    @Override
    public String doSignIn(long userId) {
        return null;
    }

    @Override
    public String initAnonymous() {
        return UUID.randomUUID().toString();
    }

}
