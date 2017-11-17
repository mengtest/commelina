package com.commelina.math24.play.match;

import akka.actor.ActorSelection;
import com.commelina.akka.dispatching.nodes.AbstractServiceActor;

/**
 * @author panyao
 * @date 2017/11/17
 */
public abstract class AbstractMatchServiceActor extends AbstractServiceActor {
    public static String GLOBAL_MATCH = "globalMatch";

    protected ActorSelection selectGlobalMatch() {
        return getContext().getSystem().actorSelection("/user/" + GLOBAL_MATCH);
    }

}
