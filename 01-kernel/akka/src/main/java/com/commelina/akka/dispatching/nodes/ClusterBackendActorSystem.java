package com.commelina.akka.dispatching.nodes;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.commelina.niosocket.message.BroadcastMessage;
import com.commelina.niosocket.message.NotifyMessage;
import com.commelina.niosocket.message.WorldMessage;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public class ClusterBackendActorSystem {

    private String frontendPath;

    private final ActorSystem system;

    private final ActorRef backendFrontend;
    public final Timeout timeout;

    public ClusterBackendActorSystem(ActorSystem system, ActorRef backendFrontend, Timeout timeout) {
        this.system = system;
        this.backendFrontend = backendFrontend;
        this.timeout = timeout;
    }

    public Object askForward(ApiRequestForward requestForward) {
        return askForward(requestForward, timeout);
    }

    public Object askForward(ApiRequestForward requestForward, Timeout timeout) {
        return PatternsCS.ask(getFrontendActor(), requestForward, timeout).toCompletableFuture().join();
    }

    public Object broadcast(BroadcastMessage messageBody) {
        return broadcast(messageBody, timeout);
    }

    public Object broadcast(BroadcastMessage message, Timeout timeout) {
        return PatternsCS.ask(getFrontendActor(), message, timeout).toCompletableFuture().join();
    }

    public Object notify(NotifyMessage messageBody) {
        return notify(messageBody, timeout);
    }

    public Object notify(NotifyMessage messageBody, Timeout timeout) {
        return PatternsCS.ask(getFrontendActor(), messageBody, timeout).toCompletableFuture().join();
    }

    public Object world(WorldMessage messageBody) {
        return world(messageBody, timeout);
    }

    public Object world(WorldMessage messageBody, Timeout timeout) {
        return PatternsCS.ask(getFrontendActor(), messageBody, timeout).toCompletableFuture().join();
    }

    public ActorSystem getActorSystem() {
        return system;
    }

    void registerRouterFronted(String routerFrontedPath) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(frontendPath));
        frontendPath = routerFrontedPath;
    }

    void removeRouterFronted() {
        frontendPath = null;
    }

    public ActorSelection getFrontendActor() {
        if (Strings.isNullOrEmpty(frontendPath)) {
            throw new ClusterFrontendException();
        }
        return system.actorSelection(frontendPath);
    }

    public ActorRef getBackendFrontend() {
        return backendFrontend;
    }
}