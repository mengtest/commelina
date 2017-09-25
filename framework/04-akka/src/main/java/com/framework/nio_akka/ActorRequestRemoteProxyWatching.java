package com.framework.nio_akka;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.framework.akka.ActorRemoteProxyClientHander;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestLogin;
import com.framework.message.ResponseMessage;
import com.framework.message.ResponseMessageDomain;
import com.framework.niosocket.ReplyUtils;
import io.netty.channel.ChannelHandlerContext;
import scala.concurrent.duration.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/8/29.
 */
@Deprecated
public abstract class ActorRequestRemoteProxyWatching extends AbstractActor implements ActorRemoteProxyClientHander {

    protected final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    final int domain;
    final String remotePath;
    protected final ChannelHandlerContext context;
    ActorRef remoteRouterActor = null;
    private AbstractActor.Receive active;

    public ActorRequestRemoteProxyWatching(final int domain, final String remotePath, final ChannelHandlerContext context) {
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
                .match(ApiRequestLogin.class, this::onRequest)
                // 回复消息
                .match(ResponseMessage.class, this::reply)
                // 回复消息到自定义 domain 下
                .match(ResponseMessageDomain.class, this::reply)
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

    @Override
    public final void onRequest(ApiRequestLogin request) {
        remoteRouterActor.forward(request, getContext());
    }

    @Override
    public final void reply(ResponseMessage message) {
        ReplyUtils.reply(context, () -> domain, () -> 0, message);
    }

    @Override
    public final void reply(ResponseMessageDomain r) {
        ReplyUtils.reply(context, r.getDomain(), () -> 0, r.getMessage());
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

    public static Props props(Class<? extends ActorRequestRemoteProxyWatching> clazz, int domain, String remotePath, ChannelHandlerContext context) {
        return Props.create(clazz, domain, remotePath, context);
    }

}
