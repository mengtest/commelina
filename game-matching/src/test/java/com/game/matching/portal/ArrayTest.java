package com.game.matching.portal;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by @panyao on 2017/8/17.
 */
public class ArrayTest {
    public static void main(String[] args) {
        Queue<Long> userIds = new ArrayDeque<>();
        userIds.offer(1l);

        for (int i = 0; userIds.iterator().hasNext(); i++) {
            final long nextUserId = userIds.iterator().next();
            System.out.println(i + ":" + nextUserId);
        }

    }
}
