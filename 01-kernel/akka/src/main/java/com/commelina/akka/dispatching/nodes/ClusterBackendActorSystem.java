package com.commelina.akka.dispatching.nodes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.commelina.niosocket.message.BroadcastMessage;
import com.commelina.niosocket.message.NotifyMessage;
import com.commelina.niosocket.message.WorldMessage;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public class ClusterBackendActorSystem {

    public static final ClusterBackendActorSystem INSTANCE = new ClusterBackendActorSystem(null);

//    private final Logger logger = LoggerFactory.getLogger(ClusterChildNodeSystem.class);

    private ActorRef clusterRouterFrontend;

    private ActorSystem system;

    public final Timeout timeout;

    public ClusterBackendActorSystem(Timeout timeout) {
        this.timeout = timeout;
    }

    public Object askForward(ApiRequestForward requestForward) {
        return askForward(requestForward, timeout);
    }

    public Object askForward(ApiRequestForward requestForward, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, requestForward, timeout).toCompletableFuture().join();
    }

    public Object broadcast(BroadcastMessage messageBody) {
        return broadcast(messageBody, timeout);
    }

    public Object broadcast(BroadcastMessage message, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, message, timeout).toCompletableFuture().join();
    }

    public Object notify(NotifyMessage messageBody) {
        return notify(messageBody, timeout);
    }

    public Object notify(NotifyMessage messageBody, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, messageBody, timeout).toCompletableFuture().join();
    }

    public Object world(WorldMessage messageBody) {
        return world(messageBody, timeout);
    }

    public Object world(WorldMessage messageBody, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, messageBody, timeout).toCompletableFuture().join();
    }

    void registerRouterFronted(ActorRef routerFronted) {
        clusterRouterFrontend = routerFronted;
    }

}