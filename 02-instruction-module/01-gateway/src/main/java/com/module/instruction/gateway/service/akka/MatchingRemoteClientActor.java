package com.game.instruction.gateway.service.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * Created by @panyao on 2017/8/24.
 */
public class MatchingRemoteClientActor extends AbstractActor {

    private final String remotePath;

    public MatchingRemoteClientActor(String remotePath) {
        this.remotePath = remotePath;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public Receive createReceive() {
        return null;
    }

    public static Props props(String remotePath) {
        return Props.create(MatchingRemoteClientActor.class, remotePath);
    }
}
