package com.nexus.maven.netty.socket;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.google.common.collect.Maps;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.MemberOfflineEvent;
import com.nexus.maven.proto.SYSTEM_CODE_CONSTANTS;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.Map;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ActorAkkaContext implements RouterContext {

    private final ActorSystem system = ActorSystem.create("akkaContext");

    /**
     * apiName -> ActorWithApiHandler
     */
    private final Map<String, ActorWithApiHandler> ROUTERS = Maps.newLinkedHashMap();

    private final Map<ChannelId, Map<String, ActorRef>> CHANNEL_ACTORS = Maps.newLinkedHashMap();

    // 初始化本机的 router
    public final void initRouters(final Map<String, ActorWithApiHandler> actorWithApiHandlers) {
        for (Map.Entry<String, ActorWithApiHandler> entry : actorWithApiHandlers.entrySet()) {
            ROUTERS.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void doRequestHandler(ChannelHandlerContext ctx, ApiRequest apiRequest) {
        Map<String, ActorRef> actorRefMap = CHANNEL_ACTORS.get(ctx.channel().id());
        if (actorRefMap != null) {
            ActorRef actorRef1 = actorRefMap.get(apiRequest.getApiPath());
            // 远程复用 actor
            if (actorRef1 != null) {
                actorRef1.tell(apiRequest, null);
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
        for (Map.Entry<String, ActorWithApiHandler> entry : ROUTERS.entrySet()) {
            ChannelOutputHandler responseContext = new ChannelOutputHandler();
            responseContext.channelHandlerContext = ctx;

            ActorRef actorRef2 = system.actorOf(entry.getValue().getProps(responseContext));

            Map<String, ActorRef> actorRefMap1 = CHANNEL_ACTORS.get(ctx.channel().id());
            if (actorRefMap1 == null) {
                actorRefMap1 = Maps.newLinkedHashMap();
            }
            actorRefMap1.put(entry.getKey(), actorRef2);
            CHANNEL_ACTORS.put(ctx.channel().id(), actorRefMap1);
            actorRef2.tell(new ActorMemberOnlineEvent(), null);
        }
    }

    @Override
    public void offlineEvent(long userId, ChannelHandlerContext ctx) {
        // 没有登录的下线通知，对于业务层来说没有意义，忽略
        if (userId <= 0) {
            return;
        }
        Map<String, ActorRef> actorRefMap = CHANNEL_ACTORS.remove(ctx.channel().id());
        if (actorRefMap != null) {
            for (ActorRef actorRef : actorRefMap.values()) {
                // 用户下线事件
                actorRef.tell(new MemberOfflineEvent(userId), null);
            }
        }
    }

    @Override
    public void exceptionEvent(ChannelHandlerContext ctx, Throwable cause) {
        // FIXME: 2017/8/29 还没有处理的
        throw new RuntimeException(cause);
    }

}


