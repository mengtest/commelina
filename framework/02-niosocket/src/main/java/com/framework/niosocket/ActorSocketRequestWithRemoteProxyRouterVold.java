package com.framework.niosocket;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.message.ApiLoginRequest;
import com.framework.message.ApiRequest;
import com.framework.message.*;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/8/29.
 */
@Deprecated
public abstract class ActorSocketRequestWithRemoteProxyRouterVold extends AbstractActor implements ActorRequestWatching, ActorSocketMemberEvent {

    protected final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final int domain;
    final String remotePath;
    protected final ChannelOutputHandler context;
    ActorRef remoteRouterActor = null;
    private Receive active;

    public ActorSocketRequestWithRemoteProxyRouterVold(final int domain, final String remotePath, final ChannelOutputHandler context) {
        this.domain = domain;
        this.remotePath = remotePath;
        this.context = context;

        // FIXME: 2017/8/28 待测试
        // 明天把 request response 和 notify 分开
//        先改这里
        this.active = receiveBuilder()
                // 请求事件
                .match(ApiRequest.class, this::onRequest)
                // 转发远程 router
                .match(ApiLoginRequest.class, r -> remoteRouterActor.forward(r, getContext()))
                // 告诉远程的 server 用户重新上线了
                .match(SocketMemberOfflineEvent.class, r -> remoteRouterActor.tell(r, getSelf()))
                // 告诉远程的 server 用户下线了
                .match(SocketMemberOfflineEvent.class, r -> remoteRouterActor.tell(r, getSelf()))
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
                .match(SocketMemberOnlineEvent.class, this::onOnlineEvent)
                // 下线事件
                .match(SocketMemberOfflineEvent.class, this::onOfflineEvent)
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

    public void onOfflineEvent(SocketMemberOfflineEvent offlineEvent) {
        getSelf().tell(new SocketMemberOfflineEvent(), getSelf());
    }

    @Override
    public void onOnlineEvent(SocketMemberOnlineEvent onlineEvent) {
        long userId = ContextAdapter.getLoginUserId(context.getRawContext().channel().id());
        if (userId > 0) {
            // 由自己的  router 发送到远程
//            getSelf().tell(new SocketMemberOnlineEvent(), getSelf());
            return;
        }
        log.info("nothing to do , channel id:" + context.getRawContext().channel().id().asShortText());
    }

    public static Props props(Class<? extends ActorSocketRequestWithRemoteProxyRouterVold> clazz, int domain, String remotePath, ChannelOutputHandler context) {
        return Props.create(clazz, domain, remotePath, context);
    }

}
