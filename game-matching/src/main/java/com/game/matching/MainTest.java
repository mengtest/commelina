package com.game.matching;

import com.framework.message.JsonMessageProvider;

/**
 * Created by @panyao on 2017/9/12.
 */
public class MainTest {

    public static void main(String[] args) {
        System.out.println(new String(JsonMessageProvider.produceMessageForKV("matchUserCount", 1).getBytes()));
    }

}
