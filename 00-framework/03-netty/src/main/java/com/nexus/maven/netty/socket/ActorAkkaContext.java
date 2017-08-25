package com.nexus.maven.netty.socket;

import akka.actor.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.nexus.maven.core.message.*;
import io.netty.channel.ChannelHandlerContext;
import io.socket.netty.proto.SocketNettyProtocol;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ActorAkkaContext {

    private final ActorSystem system = ActorSystem.create("akkaContext");
    private BiMap<String, ActorWithApiRequest> ROUTERS = HashBiMap.create(4);

    void apiRouter(final ChannelHandlerContext ctx, final ApiRequest apiRequest) {
        ActorWithApiRequest actorWithApiRequest = ROUTERS.get(apiRequest.getApiName());
        if (actorWithApiRequest == null) {
            MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                    .createErrorMessage(SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RPC_API_NOT_FOUND_VALUE);
            return;
        }

        final ActorResponseContext context = new ActorResponseContext();
        context.channelHandlerContext = ctx;
        ActorRef actorRef = system.actorOf(actorWithApiRequest.getProps(context));
        actorRef.tell(apiRequest, null);
    }

    public void initRouters(List<ActorWithApiRequest> actorWithApiRequests) {
        for (ActorWithApiRequest actorWithApiRequest : actorWithApiRequests) {
            ROUTERS.put(actorWithApiRequest.getApiName(), actorWithApiRequest);
        }
    }

    public void initNotify(List<ActorWithNotify> actorWithNotifies) {

    }

    public static final class RemoteProxyActor extends AbstractActor {
//        static final List<String> remoteApiList = Lists.newArrayList();

        final int domain;
        final String remotePath;
        final ActorResponseContext context;
        ActorSelection remoteRouterActor;

        public RemoteProxyActor(int domain, String remotePath, ActorResponseContext context) {
            this.domain = domain;
            this.remotePath = remotePath;
            this.context = context;
        }

        @Override
        public void preStart() throws Exception {
            super.preStart();
            // 获取远程的 actor
            remoteRouterActor = getContext().system().actorSelection(remotePath);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(ApiRequest.class, request -> {
                        remoteRouterActor.forward(request, getContext());
                    })
                    .match(ResponseMessage.class, responseMessage -> {
                        context.writeAndFlush(domain, responseMessage.getMessage());
                        // 响应客户端后就销毁当前的 actor
                        getContext().stop(getSelf());
                    })
                    .build();
        }

        public static Props props(int domain, String remotePath, ActorResponseContext context) {
            return Props.create(RemoteProxyActor.class, domain, remotePath, context);
        }

    }

    public static final class RequestActor extends AbstractActor {
        final int domain;
        final RequestEvent requestEvent;
        final ActorResponseContext context;

        public RequestActor(int domain, RequestEvent requestEvent, ActorResponseContext context) {
            this.domain = domain;
            this.requestEvent = requestEvent;
            this.context = context;
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(ApiRequest.class, request -> {
                        context.writeAndFlush(domain, requestEvent.onRequest(request));
                        // 执行后销毁
                        getContext().stop(getSelf());
                    })
                    .build();
        }

        public static Props props(int domain, ActorResponseContext context, RequestEvent requestEvent) {
            return Props.create(RequestActor.class, domain, context, requestEvent);
        }

        public interface RequestEvent {
            MessageBus onRequest(ApiRequest request);
        }

    }

    private static class RemoteNotifyActor extends AbstractActor {
        final int domain;
        final String remotePath;

        ActorSelection remoteNotifyActor;

        public RemoteNotifyActor(int domain, String remotePath) {
            this.domain = domain;
            this.remotePath = remotePath;
        }

        @Override
        public void preStart() throws Exception {
            super.preStart();
            // 获取远程的 actor
            remoteNotifyActor = getContext().system().actorSelection(remotePath);
            remoteNotifyActor.tell("ping", getSender());
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder()
                    .match(NotifyMessage.class, n -> MessageAdapter.addNotify(domain, n))
                    .match(WorldMessage.class, w -> MessageAdapter.addWorld(domain, w))
                    .match(BroadcastMessage.class, b -> MessageAdapter.addBroadcast(domain, b))
                    .build();
        }

        public static Props props(int domain, String remotePath) {
            return Props.create(RequestActor.class, domain, remotePath);
        }

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


