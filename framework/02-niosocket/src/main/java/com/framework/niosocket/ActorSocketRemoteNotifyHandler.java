package com.framework.niosocket;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/9/7.
 */
public class ActorSocketRemoteNotifyHandler extends AbstractActor implements ActorSocketMemberEvent {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final int domain;
    final String remotePath;
    ActorRef remoteRouterActor = null;
    private final AbstractActor.Receive active;

    public ActorSocketRemoteNotifyHandler(final int domain, final String remotePath) {
        this.domain = domain;
        this.remotePath = remotePath;

        this.active = receiveBuilder()
                .match(SocketMemberOnlineEvent.class, this::onOnlineEvent)
                .match(SocketMemberOfflineEvent.class, this::onOfflineEvent)
                .match(Terminated.class, t -> {
                    log.info("Matching terminated");
                    sendIdentifyRequest();
                    getContext().unbecome();
                })
                .match(ReceiveTimeout.class, message -> {
                    // ignore
                    log.info("ignore");
                })
                .build();

        sendIdentifyRequest();
    }

    private void sendIdentifyRequest() {
        getContext().actorSelection(remotePath).tell(new Identify(remotePath), self());
        getContext()
                .system()
                .scheduler()
                .scheduleOnce(Duration.create(3, SECONDS), self(),
                        ReceiveTimeout.getInstance(), getContext().dispatcher(), self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ActorIdentity.class, identity -> {
                    remoteRouterActor = identity.ref().get();
                    if (remoteRouterActor == null) {
                        log.info("Remote matching actor not available: " + remotePath);
                    } else {
                        getContext().watch(remoteRouterActor);
                        getContext().become(active, true);
                    }
                })
                .match(ReceiveTimeout.class, x -> sendIdentifyRequest())
                .build();
    }

    public static Props props(int domain, String remotePath) {
        return Props.create(ActorSocketRemoteNotifyHandler.class, domain, remotePath);
    }

    @Override
    public void onOnlineEvent(SocketMemberOnlineEvent onlineEvent) {

    }

    @Override
    public void onOfflineEvent(SocketMemberOfflineEvent offlineEvent) {

    }
}
