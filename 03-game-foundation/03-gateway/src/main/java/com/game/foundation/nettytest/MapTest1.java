package com.game.foundation.nettytest;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

/**
 * Created by @panyao on 2017/8/8.
 */
public class MapTest1 {
    public static void main(String[] args) {
        BiMap<Integer, Long> loginUsers = HashBiMap.create(200);

        System.out.println(loginUsers.forcePut(1, Long.valueOf(2L)));
        System.out.println(loginUsers.forcePut(1, Long.valueOf(2L)));
        System.out.println(loginUsers.put(2, 3L));
        System.out.println(loginUsers.remove(1));
        System.out.println(loginUsers);


    }
}
