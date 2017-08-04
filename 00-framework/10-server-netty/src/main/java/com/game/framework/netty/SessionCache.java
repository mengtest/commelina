package com.game.framework.netty;

import org.springframework.stereotype.Component;

import java.util.BitSet;

/**
 * Created by @panyao on 2017/8/3.
 */
@Component
public class SessionCache {
    private BitSet set = new BitSet(10000);

}
