package com.framework.niosocket;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.framework.message.ApiRequest;
import com.framework.message.RequestArg;
import com.framework.proto.Arg;
import com.framework.proto.SYSTEM_CODE_CONSTANTS;
import com.framework.proto.SocketASK;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ActorContext implements RouterContext {

    private final ActorSystem system = ActorSystem.create("akkaContext");

    /**
     * apiPathCode -> ActorRequest
     */
    private final Map<Integer, ActorRequest> ROUTERS = Maps.newLinkedHashMap();

    // 请求 actors
    private final Map<ChannelId, Map<Integer, ActorRef>> CHANNEL_REQUEST_ACTORS = Maps.newLinkedHashMap();

    // 通知  actors
    private ActorSocketMemberEvent memberEvent;

    // 初始化 router
    final void initRouters(final Map<Integer, ActorRequest> handlers) {
        for (Map.Entry<Integer, ActorRequest> entry : handlers.entrySet()) {
            ROUTERS.put(entry.getKey(), entry.getValue());
        }
    }

    final void setMemberEvent(ActorSocketMemberEvent memberEvent) {
        this.memberEvent = memberEvent;
    }

    @Override
    public void doRequestHandler(ChannelHandlerContext ctx, final SocketASK socketASK) {
        Map<Integer, ActorRef> actorRefMap = CHANNEL_REQUEST_ACTORS.get(ctx.channel().id());
        if (actorRefMap != null) {
            ActorRef actorRef1 = actorRefMap.get(socketASK.getApiPathCode());
            // 远程复用 actor
            if (actorRef1 != null) {
                final RequestArg[] args = new RequestArg[socketASK.getArgsList().size()];
                for (int i = 0; i < socketASK.getArgsList().size(); i++) {
                    Arg arg = socketASK.getArgsList().get(i);
                    args[i] = new RequestArg(arg.getValue(), RequestArg.DATA_TYPE.valueOf(arg.getDataType().name()));
                }
                actorRef1.tell(ApiRequest.newApiRequest(() -> socketASK.getApiOpcode(), socketASK.getVersion(), args), null);
                return;
            }
        }
        Object msg = MessageResponseProvider.DEFAULT_MESSAGE_RESPONSE
                .createErrorMessage(SYSTEM_CODE_CONSTANTS.RPC_API_NOT_FOUND_VALUE);

        ctx.writeAndFlush(msg);
        // FIXME: 2017/8/29 返回值未处理
    }

    @Override
    public void onlineEvent(ChannelHandlerContext ctx) {
        // 给用户生成单独的 request 事件
        for (Map.Entry<Integer, ActorRequest> entry : ROUTERS.entrySet()) {
            ChannelOutputHandler responseContext = new ChannelOutputHandler();
            responseContext.channelHandlerContext = ctx;

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
        memberEvent.onOfflineEvent(event);
    }

    @Override
    public void exceptionEvent(ChannelHandlerContext ctx, Throwable cause) {
        // FIXME: 2017/8/29 还没有处理的
        throw new RuntimeException(cause);
    }

}


