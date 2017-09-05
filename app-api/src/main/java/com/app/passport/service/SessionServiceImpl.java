package com.app.passport.service;

import org.springframework.stereotype.Repository;

/**
 * Created by @panyao on 2017/9/1.
 */
@Repository
public class SessionServiceImpl implements SessionService {

    @Override
    public long createNewToken(long userId) {
        return 0;
    }

    @Override
    public long validToken(long sid) {
        return 0;
    }

    @Override
    public void remove(long sid) {

    }
    
}
