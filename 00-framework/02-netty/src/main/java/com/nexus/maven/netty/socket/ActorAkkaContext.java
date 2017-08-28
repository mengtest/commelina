package com.nexus.maven.netty.socket;

import akka.actor.*;
import com.google.common.collect.Maps;
import com.nexus.maven.core.message.*;
import com.nexus.maven.proto.SYSTEM_CODE_CONSTANTS;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import scala.concurrent.duration.Duration;

import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ActorAkkaContext implements RouterContext {

    private final ActorSystem system = ActorSystem.create("akkaContext");

    /**
     * apiName -> ActorWithApiHandler
     */
    private final Map<String, ActorWithApiHandler> ROUTERS = Maps.newLinkedHashMap();
    /**
     * apiName -> ActorWithApiRemoteHandler
     */
    private final Map<String, ActorWithApiRemoteHandler> REMOTE_ROUTERS = Maps.newLinkedHashMap();

    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS = Maps.newLinkedHashMap();
//    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS0 = Maps.newLinkedHashMap();
//    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS1 = Maps.newLinkedHashMap();
//    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS2 = Maps.newLinkedHashMap();
//    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS3 = Maps.newLinkedHashMap();
//    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS4 = Maps.newLinkedHashMap();
//    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS5 = Maps.newLinkedHashMap();
//    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS6 = Maps.newLinkedHashMap();

    // 初始化本机的 router
    public final void initRouters(final Map<String, ActorWithApiHandler> actorWithApiHandlers) {
        for (Map.Entry<String, ActorWithApiHandler> entry : actorWithApiHandlers.entrySet()) {
            ROUTERS.put(entry.getKey(), entry.getValue());
        }
    }

    public final void initRemoteRouters(final Map<String, ActorWithApiRemoteHandler> actorWithApiHandlers) {
        for (Map.Entry<String, ActorWithApiRemoteHandler> entry : actorWithApiHandlers.entrySet()) {
            REMOTE_ROUTERS.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void doRequestHandler(ChannelHandlerContext ctx, ApiRequest apiRequest) {
        Map<String, ActorRef> actorRefMap = CHANNEL_ACTORS.get(ctx.channel().id());
        if (actorRefMap != null) {
            ActorRef actorRef1 = actorRefMap.get(apiRequest.getApiName());
            // 远程复用 actor
            if (actorRef1 != null) {
                actorRef1.tell(apiRequest, null);
                return;
            }
        }
        Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                .createErrorMessage(SYSTEM_CODE_CONSTANTS.RPC_API_NOT_FOUND_VALUE);
        ctx.writeAndFlush(msg);
    }

    @Override
    public void onlineEvent(ChannelHandlerContext ctx) {
        for (Map.Entry<String, ActorWithApiHandler> entry : ROUTERS.entrySet()) {
            ActorOutputContext responseContext = new ActorOutputContext();
            responseContext.channelHandlerContext = ctx;

            ActorRef actorRef2 = system.actorOf(RequestRouterActor.props(
                    entry.getValue().getDomain(), responseContext, entry.getValue().getRouterEvent()));
            Map<String, ActorRef> actorRefMap1 = Maps.newLinkedHashMap();
            actorRefMap1.put(entry.getKey(), actorRef2);
            CHANNEL_ACTORS.put(ctx.channel().id(), actorRefMap1);
            actorRef2.tell("online", null);
        }

        for (Map.Entry<String, ActorWithApiRemoteHandler> entry : REMOTE_ROUTERS.entrySet()) {
            ActorOutputContext responseContext = new ActorOutputContext();
            responseContext.channelHandlerContext = ctx;

            ActorRef actorRef2 = system.actorOf(RemoteProxyActor.props(
                    entry.getValue().getDomain(), entry.getValue().getRemoteActorPath(), responseContext, entry.getValue().getRouterEvent()));
            Map<String, ActorRef> actorRefMap1 = Maps.newLinkedHashMap();
            actorRefMap1.put(entry.getKey(), actorRef2);
            CHANNEL_ACTORS.put(ctx.channel().id(), actorRefMap1);
            actorRef2.tell("online", null);
        }
    }

    @Override
    public void offlineEvent(long userId, ChannelHandlerContext ctx) {
        Map<String, ActorRef> actorRefMap = CHANNEL_ACTORS.remove(ctx.channel().id());
        if (actorRefMap != null) {
            for (ActorRef actorRef : actorRefMap.values()) {
                actorRef.tell("offline", null);
            }
        }
    }

    @Override
    public void exceptionEvent(ChannelHandlerContext ctx, Throwable cause) {

    }

    public static final class RemoteProxyActor extends AbstractActor {

        final int domain;
        final String remotePath;
        final ActorOutputContext context;
        final ActorWithApiRemoteHandler.RequestEvent requestEvent;
        ActorRef remoteRouterActor = null;
        private Receive active;

        public RemoteProxyActor(int domain, String remotePath, ActorOutputContext context, ActorWithApiRemoteHandler.RequestEvent requestEvent) {
            this.domain = domain;
            this.remotePath = remotePath;
            this.context = context;
            this.requestEvent = requestEvent;

            // FIXME: 2017/8/28 待测试
            this.active = receiveBuilder()
                    .match(ApiRequest.class, apiRequest -> RemoteProxyActor.this.requestEvent.onRequest(apiRequest, RemoteProxyActor.this.context, getSelf()))
                    .match(AkkaActorApiRequest.class, request -> remoteRouterActor.forward(request, getContext()))
                    .match(ResponseMessage.class, responseMessage -> RemoteProxyActor.this.context.writeAndFlush(RemoteProxyActor.this.domain, responseMessage.getMessage()))
                    .match(NotifyMessage.class, n -> MessageAdapter.addNotify(RemoteProxyActor.this.domain, n))
                    .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(RemoteProxyActor.this.domain, b))
                    .match(WorldMessage.class, w -> MessageAdapter.addWorld(RemoteProxyActor.this.domain, w))
                    .match(Terminated.class, terminated -> {
                        System.out.println("Matching terminated");
                        sendIdentifyRequest();
                        getContext().unbecome();
                    })
                    .match(ReceiveTimeout.class, message -> {
                        // ignore
                    })
                    .matchEquals("online", s -> getContext().stop(getSelf()))
                    .matchEquals("offline", s -> getContext().stop(getSelf()))
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
                        remoteRouterActor = identity.getActorRef().get();
                        if (remoteRouterActor == null) {
                            System.out.println("Remote matching actor not available: " + remotePath);
                        } else {
                            getContext().watch(remoteRouterActor);
                            getContext().become(active, true);
                        }
                    })
                    .match(ReceiveTimeout.class, x -> sendIdentifyRequest())
                    .build();
        }

        static Props props(int domain, String remotePath, ActorOutputContext context, ActorWithApiRemoteHandler.RequestEvent remoteRouterEvent) {
            return Props.create(RemoteProxyActor.class, domain, remotePath, context, remoteRouterEvent);
        }

    }

    public static final class RequestRouterActor extends AbstractActor {
        final int domain;
        final ActorWithApiHandler.RequestEvent requestEvent;
        final ActorOutputContext context;

        public RequestRouterActor(int domain, ActorOutputContext context, ActorWithApiHandler.RequestEvent requestEvent) {
            this.domain = domain;
            this.context = context;
            this.requestEvent = requestEvent;
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(ApiRequest.class, apiRequest -> requestEvent.onRequest(apiRequest, context, getSelf()))
                    .match(ResponseMessage.class, responseMessage -> context.writeAndFlush(domain, responseMessage.getMessage()))
                    .build();
        }

        static Props props(int domain, ActorOutputContext context, ActorWithApiHandler.RequestEvent requestEvent) {
            return Props.create(RequestRouterActor.class, domain, context, requestEvent);
        }

    }

}


