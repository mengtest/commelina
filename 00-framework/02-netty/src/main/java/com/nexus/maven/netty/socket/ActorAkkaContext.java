package com.nexus.maven.netty.socket;

import akka.actor.*;
import com.google.common.collect.Maps;
import com.nexus.maven.core.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.socket.netty.proto.SocketNettyProtocol;
import scala.concurrent.duration.Duration;

import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ActorAkkaContext {

    private final ActorSystem system = ActorSystem.create("akkaContext");
    private Map<String, ActorWithApiRequest> LOCAL_ROUTERS = Maps.newLinkedHashMap();

    private Map<String, ActorWithApiRemote> REMOTE_ROUTERS = Maps.newLinkedHashMap();
    private Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS = Maps.newHashMap();

    void apiRouter(final ChannelHandlerContext ctx, final ApiRequest apiRequest) {
        ActorWithApiRequest actorWithApiRequest = LOCAL_ROUTERS.get(apiRequest.getApiName());
        if (actorWithApiRequest != null) {
            ApiRequestContext requestContext = new ApiRequestContext();
            requestContext.apiRequest = apiRequest;
            ActorResponseContext responseContext = new ActorResponseContext();
            responseContext.channelHandlerContext = ctx;
            requestContext.context = responseContext;
            // 本机总是用新的 actor
            ActorRef actorRef2 = system.actorOf(actorWithApiRequest.getProps());
            actorRef2.tell(requestContext, null);
            return;
        }

        Map<String, ActorRef> actorRefMap = CHANNEL_ACTORS.get(ctx.channel().id());
        do {
            if (actorRefMap != null) {
                ActorRef actorRef1 = actorRefMap.get(apiRequest.getApiName());
                // 远程复用 actor
                if (actorRef1 != null) {
                    actorRef1.tell(apiRequest, null);
                    break;
                }
            }

            ActorWithApiRemote remote = REMOTE_ROUTERS.get(apiRequest.getApiName());
            if (remote == null) {
                Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                        .createErrorMessage(SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RPC_API_NOT_FOUND_VALUE);
                ctx.writeAndFlush(msg);
                return;
            }

            final ActorResponseContext context = new ActorResponseContext();
            context.channelHandlerContext = ctx;
            ActorRef actorRef2 = system.actorOf(remote.getProps(context));
            Map<String, ActorRef> actorRefMap1 = Maps.newLinkedHashMap();
            actorRefMap1.put(apiRequest.getApiName(), actorRef2);
            CHANNEL_ACTORS.put(ctx.channel().id(), actorRefMap1);
            actorRef2.tell(apiRequest, null);
        } while (false);
    }

    void unRegister(final ChannelHandlerContext ctx) {
        Map<String, ActorRef> actorRefMap = CHANNEL_ACTORS.remove(ctx.channel().id());
        if (actorRefMap != null) {
            for (ActorRef actorRef : actorRefMap.values()) {
                actorRef.tell("stop", null);
            }
        }
    }

    // 初始化本机的 router
    public void initLocalRouters(List<ActorWithApiRequest> actorWithApiRedirects) {
        for (ActorWithApiRequest actorWithApiRedirect : actorWithApiRedirects) {
            LOCAL_ROUTERS.put(actorWithApiRedirect.getApiName(), actorWithApiRedirect);
        }
    }

    public void initRemoteRouters(List<ActorWithApiRemote> actorWithApiRemotes) {
        for (ActorWithApiRemote actorWithApiRemote : actorWithApiRemotes) {
            REMOTE_ROUTERS.put(actorWithApiRemote.getApiName(), actorWithApiRemote);
        }
    }

    public static final class RemoteProxyActor extends AbstractActor {

        final int domain;
        final String remotePath;
        final ActorResponseContext context;
        ActorRef remoteRouterActor = null;

        public RemoteProxyActor(int domain, String remotePath, ActorResponseContext context) {
            this.domain = domain;
            this.remotePath = remotePath;
            this.context = context;
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
                        remoteRouterActor = identity.getRef();
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

        private Receive active = receiveBuilder()
                .match(ApiRequest.class, request -> {
                    RequestArg pathArg = request.getArg(0);
                    if (pathArg == null) {
                        // FIXME: 2017/8/25 远程路由地址
                    }
                    String remoteApiPath = pathArg.getAsString();

                    RequestArg[] args = new RequestArg[request.getArgs().length - 1];
                    for (int i = 1; i < request.getArgs().length; i++) {
                        args[i - 1] = request.getArgs()[i];
                    }

                    final ApiRequest newApiRequest = new ApiRequest(remoteApiPath, request.getVersion(), args);
                    remoteRouterActor.forward(newApiRequest, getContext());
                }).match(ResponseMessage.class, responseMessage -> RemoteProxyActor.this.context.writeAndFlush(RemoteProxyActor.this.domain, responseMessage.getMessage()))
                .match(NotifyMessage.class, n -> MessageAdapter.addNotify(domain, n))
                .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(domain, b))
                .match(WorldMessage.class, w -> MessageAdapter.addWorld(domain, w))
                .match(Terminated.class, terminated -> {
                    System.out.println("Matching terminated");
                    sendIdentifyRequest();
                    getContext().unbecome();
                })
                .match(ReceiveTimeout.class, message -> {
                    // ignore
                })
                .build();

        public static Props props(int domain, String remotePath, ActorResponseContext context) {
            return Props.create(RemoteProxyActor.class, domain, remotePath, context);
        }

    }

    public static final class RequestActor extends AbstractActor {
        final int domain;
        final RequestEvent requestEvent;

        public RequestActor(int domain, RequestEvent requestEvent) {
            this.domain = domain;
            this.requestEvent = requestEvent;
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(ApiRequestContext.class, requestContext -> {
                        requestContext.context.writeAndFlush(domain, requestEvent.onRequest(requestContext.apiRequest));
                        // 执行后销毁
                        getContext().stop(getSelf());
                    })
                    .matchEquals("stop", s -> getContext().stop(getSelf()))
                    .build();
        }

        public static Props props(int domain, RequestEvent requestEvent) {
            return Props.create(RequestActor.class, domain, requestEvent);
        }

        public interface RequestEvent {
            MessageBus onRequest(ApiRequest request);
        }

    }

    public static class ApiRequestContext {
        ApiRequest apiRequest;
        ActorResponseContext context;
    }

//    private static class RemoteApiAcotr extends AbstractActor {
//
//        @Override
//        public Receive createReceive() {
//            return null;
//        }
//
//    }

}


