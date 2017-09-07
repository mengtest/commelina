package com.framework.niosocket;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka.MemberOfflineEvent;
import com.framework.akka.MemberOnlineEvent;
import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/9/7.
 */
public class ActorNotifyRemoteHandler extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final int domain;
    final String remotePath;
    ActorRef remoteRouterActor = null;
    private final AbstractActor.Receive active;

    public ActorNotifyRemoteHandler(final int domain, final String remotePath) {
        this.domain = domain;
        this.remotePath = remotePath;

        this.active = receiveBuilder()
                // 用户离线事件 直接发送到远程
                .match(MemberOfflineEvent.class, off -> remoteRouterActor.tell(off, getSelf()))
                // 用户上线事件，直接发送到远程
                .match(MemberOnlineEvent.class, on -> remoteRouterActor.tell(on, getSelf()))
                // 通知
                .match(NotifyMessage.class, n -> MessageAdapter.addNotify(domain, n))
                // 广播
                .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(domain, b))
                // 世界消息
                .match(WorldMessage.class, w -> MessageAdapter.addWorld(domain, w))
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
        return Props.create(ActorNotifyRemoteHandler.class, domain, remotePath);
    }

}
