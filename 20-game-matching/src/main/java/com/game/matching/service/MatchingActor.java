package com.game.matching.service;

import akka.actor.UntypedActor;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by @panyao on 2017/8/10.
 */
public class MatchingActor extends UntypedActor {

    private final Queue<String> queue = new ArrayDeque<>();

    public void add(long userId) {

    }

    @Override
    public void onReceive(Object o) throws Throwable {

    }

}
