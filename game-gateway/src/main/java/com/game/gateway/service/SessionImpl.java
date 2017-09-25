package com.game.gateway.service;

import org.springframework.stereotype.Service;

/**
 * Created by @panyao on 2017/9/25.
 */
@Service
public class SessionImpl implements SessionInterface {

    @Override
    public boolean valid(String token) {
        return false;
    }

}
