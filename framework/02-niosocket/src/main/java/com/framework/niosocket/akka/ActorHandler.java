package com.framework.niosocket.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.framework.message.ApiRequest;
import com.framework.message.RequestArg;
import com.framework.niosocket.ChannelOutputHandler;
import com.framework.niosocket.MessageResponseProvider;
import com.framework.niosocket.RouterHandler;
import com.framework.niosocket.proto.Arg;
import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketASK;
import com.google.common.collect.Maps;
import com.typesafe.config.ConfigFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ActorHandler implements RouterHandler {

    private final ActorSystem system = ActorSystem.create("akkaRouterContext", ConfigFactory.load(("akkarequest")));
    /**
     * apiPathCode -> ActorRequestWatching
     */
    private final Map<Integer, ActorRequestWatching> ROUTERS = Maps.newLinkedHashMap();

    // 请求 actors
    private final Map<ChannelId, Map<Integer, ActorRef>> CHANNEL_REQUEST_ACTORS = Maps.newLinkedHashMap();

    // 通知  actors
    private ActorSocketMemberEvent memberEvent;

    // 初始化 router
    final void initRouters(final Map<Integer, ActorRequestWatching> handlers) {
        for (Map.Entry<Integer, ActorRequestWatching> entry : handlers.entrySet()) {
            ROUTERS.put(entry.getKey(), entry.getValue());
        }
    }

    final void setMemberEvent(ActorSocketMemberEvent memberEvent) {
        this.memberEvent = memberEvent;
    }

    @Override
    public void doRequestHandler(ChannelHandlerContext ctx, final SocketASK request) {
        Map<Integer, ActorRef> actorRefMap = CHANNEL_REQUEST_ACTORS.get(ctx.channel().id());
        if (actorRefMap != null) {
            ActorRef actorRef1 = actorRefMap.get(request.getApiCode());
            // 远程复用 actor
            if (actorRef1 != null) {
                final RequestArg[] args = new RequestArg[request.getArgsList().size()];
                for (int i = 0; i < request.getArgsList().size(); i++) {
                    Arg arg = request.getArgsList().get(i);
                    args[i] = new RequestArg(arg.getValue(), RequestArg.DATA_TYPE.valueOf(arg.getDataType().name()));
                }
                actorRef1.tell(ApiRequest.newApiRequest(() -> request.getApiMethod(), request.getVersion(), args), null);
                return;
            }
        }
        Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE.createErrorMessage(SERVER_CODE.RPC_API_NOT_FOUND);

        ctx.writeAndFlush(msg);
        // FIXME: 2017/8/29 返回值未处理
    }

    @Override
    public void onlineEvent(ChannelHandlerContext ctx) {
        // 给用户生成单独的 request 事件
        for (Map.Entry<Integer, ActorRequestWatching> entry : ROUTERS.entrySet()) {
            ChannelOutputHandler responseContext = new ChannelOutputHandler();
            responseContext.setChannelHandlerContext(ctx);

            ActorRef actorRef2 = system.actorOf(entry.getValue().getProps(responseContext));

            Map<Integer, ActorRef> actorRefMap1 = CHANNEL_REQUEST_ACTORS.get(ctx.channel().id());
            if (actorRefMap1 == null) {
                actorRefMap1 = Maps.newLinkedHashMap();
            }
            actorRefMap1.put(entry.getKey(), actorRef2);
            CHANNEL_REQUEST_ACTORS.put(ctx.channel().id(), actorRefMap1);
        }

        ActorSocketMemberEvent.SocketMemberOnlineEvent event = new ActorSocketMemberEvent.SocketMemberOnlineEvent();
        event.channelId = ctx.channel().id();
        memberEvent.onOnlineEvent(event);
    }

    @Override
    public void offlineEvent(long userId, ChannelHandlerContext ctx) {
        // 没有登录的下线通知，对于业务层来说没有意义，忽略
        if (userId <= 0) {
            return;
        }
        ActorSocketMemberEvent.SocketMemberOfflineEvent event = new ActorSocketMemberEvent.SocketMemberOfflineEvent();
        event.userId = userId;
        event.channelId = ctx.channel().id();
        memberEvent.onOfflineEvent(event);
    }

    @Override
    public void exceptionEvent(ChannelHandlerContext ctx, Throwable cause) {
        // FIXME: 2017/8/29 还没有处理的
        throw new RuntimeException(cause);
    }

}


