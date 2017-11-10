package com.commelina.match24.play.match.room;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * @author panyao
 * @date 2017/11/10
 */
public class TemporaryRoom extends AbstractActor {
    @Override
    public Receive createReceive() {
        return null;
    }

    public static Props props() {
        return Props.create(TemporaryRoom.class);
    }
}
