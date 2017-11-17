package com.commelina.math24.play.match;

import akka.actor.ActorSelection;
import akka.actor.Scheduler;
import com.commelina.akka.dispatching.nodes.AbstractServiceActor;
import scala.concurrent.ExecutionContextExecutor;

/**
 * @author panyao
 * @date 2017/11/17
 */
public abstract class AbstractMatchServiceActor extends AbstractServiceActor {

    static final String GLOBAL_MATCH = "globalMatch";
    static final String ROOM_MANAGER = "roomManager";

    protected ActorSelection selectGlobalMatch() {
        return getContext().getSystem().actorSelection("/user/" + GLOBAL_MATCH);
    }

    protected ActorSelection selectRoomManger() {
        return getContext().getSystem().actorSelection("/user/" + ROOM_MANAGER);
    }

    protected Scheduler getScheduler() {
        return getContext().getSystem().scheduler();
    }

    protected ExecutionContextExecutor getDispatcher() {
        return getContext().getSystem().dispatcher();
    }

}
