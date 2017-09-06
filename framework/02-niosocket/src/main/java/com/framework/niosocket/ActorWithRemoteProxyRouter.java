package com.framework.niosocket;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka.ActorRouterWatching;
import com.framework.akka.ApiRequestWithActor;
import com.framework.akka.MemberOfflineEvent;
import com.framework.message.*;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/8/29.
 */
public abstract class ActorWithRemoteProxyRouter extends AbstractActor implements ActorRouterWatching {

    protected final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

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
                .match(ApiRouterRequest.class, this::onRequest)
                // 转发远程 router
                .match(ApiRequestWithActor.class, r -> remoteRouterActor.forward(r, getContext()))
                // 告诉远程的 server 用户重新上线了
                .match(MemberOfflineEvent.MemberOnlineEvent.class, r -> remoteRouterActor.tell(r, getSelf()))
                // 告诉远程的 server 用户下线了
                .match(MemberOfflineEvent.class, r -> remoteRouterActor.tell(r, getSelf()))
                // 回复消息
                .match(ResponseMessage.class, r -> context.writeAndFlush(domain, r))
                // 回复消息到自定义 domain 下
                .match(ResponseMessageDomain.class, r -> context.writeAndFlush(r.getDomain(), r.getMessage()))
                // 通知
                .match(NotifyMessage.class, n -> MessageAdapter.addNotify(domain, n))
                // 广播
                .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(domain, b))
                // 世界消息
                .match(WorldMessage.class, w -> MessageAdapter.addWorld(domain, w))
                // 上线事件
                .match(MemberOnlineEvent.class, this::onOnlineEvent)
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
        getSelf().tell(new MemberOfflineEvent.MemberOnlineEvent(offlineEvent.getUserId()), getSelf());
    }

    @Override
    public void onOnlineEvent(MemberOnlineEvent onlineEvent) {
        long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
        if (userId > 0) {
            // 由自己的  router 发送到远程
            getSelf().tell(new MemberOfflineEvent.MemberOnlineEvent(userId), getSelf());
            return;
        }
        log.info("nothing to do , channel id:" + context.getRawContext().channel().id().asShortText());
    }

    public static Props props(Class<? extends ActorWithRemoteProxyRouter> clazz, int domain, String remotePath, ChannelOutputHandler context) {
        return Props.create(clazz, domain, remotePath, context);
    }

}
