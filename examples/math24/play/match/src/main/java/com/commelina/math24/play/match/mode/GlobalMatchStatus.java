package com.commelina.math24.play.match.mode;

import akka.actor.AbstractActor;
import akka.actor.Props;

/**
 * @author panyao
 * @date 2017/11/10
 */
public class GlobalMatchStatus extends AbstractActor {

    @Override
    public Receive createReceive() {
        return null;
    }

    public static Props props() {
        return Props.create(GlobalMatchStatus.class);
    }

}
