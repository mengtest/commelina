package com.framework.niosocket;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka.MemberOfflineEvent;
import com.framework.akka.MemberOnlineEvent;
import com.framework.akka.ActorRemoteForwardClientHandler;
import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.ResponseForward;
import com.framework.message.WorldMessage;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/9/7.
 */
public abstract class ActorNotifyRemoteHandler extends AbstractActor implements ActorRemoteForwardClientHandler {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final int domain;
    private final String remotePath;
    private ActorRef remoteRouterActor = null;
    private final AbstractActor.Receive active;

    public ActorNotifyRemoteHandler(final int domain, final String remotePath) {
        this.domain = domain;
        this.remotePath = remotePath;

        this.active = receiveBuilder()
                // 用户离线事件 直接发送到远程
                .match(MemberOfflineEvent.class, this::onEvent)
                // 用户上线事件，直接发送到远程
                .match(MemberOnlineEvent.class, this::onEvent)
                // 重定向逻辑
                .match(ResponseForward.class, this::onForwardEvent)
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

    @Override
    public final void onEvent(MemberOfflineEvent offlineEvent) {
        remoteRouterActor.tell(offlineEvent, getSelf());
    }

    @Override
    public final void onEvent(MemberOnlineEvent onlineEvent) {
        remoteRouterActor.tell(onlineEvent, getSelf());
    }

    @Override
    public final void reply(NotifyMessage message) {
        MessageAdapter.addNotify(domain, message);
    }

    @Override
    public final void reply(BroadcastMessage message) {
        MessageAdapter.addBroadcast(domain, message);
    }

    @Override
    public final void reply(WorldMessage message) {
        MessageAdapter.addWorld(domain, message);
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
        return Props.create(ActorNotifyRemoteHandler.class, domain, remotePath);
    }

}
