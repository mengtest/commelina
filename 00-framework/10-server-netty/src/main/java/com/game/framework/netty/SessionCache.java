package com.game.framework.netty;

import org.springframework.stereotype.Component;

import java.util.BitSet;

/**
 * Created by @panyao on 2017/8/3.
 */
@Component
public class SessionCache {
    private BitSet set = new BitSet(10000);

    private SessionInterface sessionInterface;

    public SessionInterface getSessionInterface() {
        return sessionInterface;
    }

    public void setSessionInterface(SessionInterface sessionInterface) {
        this.sessionInterface = sessionInterface;
    }

}
