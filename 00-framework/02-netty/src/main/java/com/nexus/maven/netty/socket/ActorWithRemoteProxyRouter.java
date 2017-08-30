package com.nexus.maven.netty.socket;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.nexus.maven.core.message.*;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class ActorWithRemoteProxyRouter extends AbstractActor implements ActorRouterWatching {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final int domain;
    final String remotePath;
    protected final ChannelOutputHandler context;
    ActorRef remoteRouterActor = null;
    private AbstractActor.Receive active;

    public ActorWithRemoteProxyRouter(int domain, String remotePath, ChannelOutputHandler context) {
        this.domain = domain;
        this.remotePath = remotePath;
        this.context = context;

        // FIXME: 2017/8/28 待测试
        this.active = receiveBuilder()
                // 请求事件
                .match(ApiRequest.class, this::onRequest)
                // 转发远程 router
                .match(ApiRequestWithActor.class, r -> remoteRouterActor.forward(r, getContext()))
                //
                .match(MemberOnlineEvent.class, r -> remoteRouterActor.tell(r, getSelf()))
                //
                .match(MemberOfflineEvent.class, r -> remoteRouterActor.tell(r, getSelf()))
                // 回复消息
                .match(ResponseMessage.class, r -> context.writeAndFlush(domain, r.getMessage()))
                // 通知
                .match(NotifyMessage.class, n -> MessageAdapter.addNotify(domain, n))
                // 广播
                .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(domain, b))
                // 世界消息
                .match(WorldMessage.class, w -> MessageAdapter.addWorld(domain, w))
                // 上线事件
                .match(ActorMemberOnlineEvent.class, this::onOnlineEvent)
                // 下线事件
                .match(MemberOfflineEvent.class, this::onOfflineEvent)
                .match(Terminated.class, t -> {
                    log.info("Matching terminated");
                    sendIdentifyRequest();
                    getContext().unbecome();
                })
                .match(ReceiveTimeout.class, message -> {
                    // ignore
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

    public void onOfflineEvent(MemberOfflineEvent offlineEvent) {
        getSelf().tell(new MemberOnlineEvent(offlineEvent.getUserId()), getSelf());
    }

    public static Props props(Class<? extends ActorWithRemoteProxyRouter> clazz, int domain, String remotePath, ChannelOutputHandler context) {
        return Props.create(clazz, domain, remotePath, context);
    }

}
