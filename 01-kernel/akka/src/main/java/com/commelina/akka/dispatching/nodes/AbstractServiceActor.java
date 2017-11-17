package com.commelina.akka.dispatching.nodes;

import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.commelina.akka.dispatching.Constants;
import com.commelina.akka.dispatching.proto.BackendFindEvent;
import com.commelina.akka.dispatching.proto.BackendFindFrontend;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public abstract class AbstractServiceActor extends AbstractActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    private ActorSelection frontend;

    protected ActorSelection selectFrontend() {
        if (frontend == null) {
            BackendFindFrontend backendFindFrontend = (BackendFindFrontend) PatternsCS.ask(getContext().getSystem()
                    .actorSelection(Constants.CLUSTER_BACKEND_PATH), BackendFindEvent.getDefaultInstance(), getAskForFrontendTimeout())
                    .toCompletableFuture().join();

            frontend = getContext().getSystem().actorSelection(backendFindFrontend.getFrontendAddress());
        }
        return frontend;
    }

    protected ActorSelection selectBackendFrontend() {
        return getContext().getSystem().actorSelection(Constants.CLUSTER_BACKEND_PATH);
    }

    /**
     * 获取 服务器前端的path 超时时间
     *
     * @return
     */
    protected Timeout getAskForFrontendTimeout() {
        return new Timeout(Duration.create(5, TimeUnit.SECONDS));
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }

}