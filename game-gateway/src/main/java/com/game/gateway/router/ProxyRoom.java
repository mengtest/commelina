package com.game.gateway.router;

import com.framework.akka.router.DefaultClusterActorRequestHandler;
import com.framework.akka.router.cluster.RouterFrontedClusterActor;
import com.framework.akka.router.local.AkkaLocalWorkerSystem;
import com.framework.akka.router.proto.ApiRequest;
import com.framework.akka.router.proto.ApiRequestForward;
import com.framework.core.BusinessMessage;
import com.framework.core.DefaultMessageProvider;
import com.framework.core.MessageBody;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.proto.SocketASK;
import com.game.common.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.FindRoom;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author @panyao
 * @date 2017/9/22
 */
@NioSocketRouter(forward = DOMAIN.GAME_ROOM_VALUE)
public class ProxyRoom extends DefaultClusterActorRequestHandler {

    private final MessageBody messageBody = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_UNAUTHORIZED));

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.GAME_ROOM;
    }

    @Override
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {

        boolean isExists = (Boolean) AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(FindRoom.newBuilder()
                .setRoomId(Long.valueOf(ask.getArgs(0).toStringUtf8()))
                .build());

        if (!isExists) {
            // 房间不存在
            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, ERROR_CODE.ROOM_NOT_FOUND_VALUE, messageBody);
            return false;
        }

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ReplyUtils.reply(ctx, DOMAIN.GATE_WAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

        return true;
    }

    public static class RoomRouterFrontedClusterActor extends RouterFrontedClusterActor {

        public RoomRouterFrontedClusterActor(Internal.EnumLite myRouterId) {
            super(myRouterId);
        }

        @Override
        public int selectActorSeed(ApiRequest request) {
            return (int) (Long.valueOf(request.getArgs(0).toStringUtf8()) % 2);
        }

        @Override
        public int selectActorSeed(ApiRequestForward forward) {
            return super.selectActorSeed(forward);
        }

    }

}
