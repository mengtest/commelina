package com.framework.akka.router.cluster.nodes;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.PatternsCS;
import akka.util.Timeout;
import com.framework.akka.router.proto.ActorBroadcast;
import com.framework.akka.router.proto.ActorNotify;
import com.framework.akka.router.proto.ActorWorld;
import com.framework.akka.router.proto.ApiRequestForward;
import com.framework.core.MessageBody;
import com.google.protobuf.ByteString;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.concurrent.duration.Duration;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author @panyao
 * @date 2017/9/25
 */
public class ClusterChildNodeSystem {

    public static final ClusterChildNodeSystem INSTANCE = new ClusterChildNodeSystem();

    private final Logger logger = LoggerFactory.getLogger(ClusterChildNodeSystem.class);

    private ActorRef clusterRouterFrontend;

    private ActorRef localRouterFrontend;

    private ActorSystem system;

    public static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    public Object askForward(ApiRequestForward requestForward) {
        return askForward(requestForward, DEFAULT_TIMEOUT);
    }

    public Object askForward(ApiRequestForward requestForward, Timeout timeout) {
        return PatternsCS.ask(clusterRouterFrontend, requestForward, timeout).toCompletableFuture().join();
    }

    public Object broadcast(int opcode, List<Long> userIds, MessageBody messageBody) {
        return broadcast(opcode, userIds, messageBody, DEFAULT_TIMEOUT);
    }

    public Object broadcast(int opcode, List<Long> userIds, MessageBody messageBody, Timeout timeout) {
        byte[] bytes;
        try {
            bytes = messageBody.getBytes();
        } catch (IOException e) {
            logger.error("{}", e);
            return null;
        }
        return PatternsCS.ask(clusterRouterFrontend,
                ActorBroadcast.newBuilder()
                        .setOpcode(opcode)
                        .addAllUserIds(userIds)
                        .setMessage(ByteString.copyFrom(bytes))
                        .build(), timeout)
                .toCompletableFuture().join();
    }

    public Object notify(int opcode, long userId, MessageBody messageBody) {
        return notify(opcode, userId, messageBody, DEFAULT_TIMEOUT);
    }

    public Object notify(int opcode, long userId, MessageBody messageBody, Timeout timeout) {
        byte[] bytes;
        try {
            bytes = messageBody.getBytes();
        } catch (IOException e) {
            logger.error("{}", e);
            return null;
        }
        return PatternsCS.ask(clusterRouterFrontend, ActorNotify.newBuilder()
                .setOpcode(opcode)
                .setUserId(userId)
                .setMessage(ByteString.copyFrom(bytes))
                .build(), timeout).toCompletableFuture().join();
    }

    public Object world(int opcode, MessageBody messageBody) {
        return world(opcode, messageBody, DEFAULT_TIMEOUT);
    }

    public Object world(int opcode, MessageBody messageBody, Timeout timeout) {
        byte[] bytes;
        try {
            bytes = messageBody.getBytes();
        } catch (IOException e) {
            logger.error("{}", e);
            return null;
        }
        return PatternsCS.ask(clusterRouterFrontend, ActorWorld.newBuilder()
                .setOpcode(opcode)
                .setMessage(ByteString.copyFrom(bytes))
                .build(), timeout).toCompletableFuture().join();
    }

    void registerRouterFronted(ActorRef routerFronted) {
        clusterRouterFrontend = routerFronted;
    }

    void removeRouterFronted() {
        clusterRouterFrontend = null;
    }

    void create(String clusterName, String config) {
        system = ActorSystem.create(clusterName, ConfigFactory.load(config)
                .withFallback(ConfigFactory.load("default-remote-message-bindings")));
    }

    void registerRouterFronted(Props props) {
        if (localRouterFrontend != null) {
            throw new InvalidParameterException();
        }
        localRouterFrontend = system.actorOf(props, "localRouterFrontend");
    }

}