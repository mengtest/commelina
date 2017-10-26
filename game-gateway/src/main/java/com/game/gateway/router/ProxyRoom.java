package com.game.gateway.router;

import com.github.freedompy.commelina.akka.router.DefaultClusterActorRequestHandler;
import com.github.freedompy.commelina.akka.router.cluster.RouterFrontedClusterActor;
import com.github.freedompy.commelina.akka.router.local.AkkaLocalWorkerSystem;
import com.github.freedompy.commelina.akka.router.proto.ApiRequest;
import com.github.freedompy.commelina.akka.router.proto.ApiRequestForward;
import com.github.freedompy.commelina.core.BusinessMessage;
import com.github.freedompy.commelina.core.DefaultMessageProvider;
import com.github.freedompy.commelina.core.MessageBody;
import com.github.freedompy.commelina.niosocket.ContextAdapter;
import com.github.freedompy.commelina.niosocket.NioSocketRouter;
import com.github.freedompy.commelina.niosocket.ReplyUtils;
import com.github.freedompy.commelina.niosocket.proto.SocketASK;
import com.game.gateway.proto.ChangeAccesssDoamin;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.FindRoomRequest;
import com.game.gateway.proto.FindRoomResponse;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import com.message.common.proto.DOMAIN;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author @panyao
 * @date 2017/9/22
 */
@NioSocketRouter(forward = DOMAIN.GAME_ROOM_VALUE)
public class ProxyRoom extends DefaultClusterActorRequestHandler {

    private final MessageBody messageBody = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_API_UNAUTHORIZED));

    @Override
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {
        do {
            ByteString roomId = ask.getArgs(0);
            if (roomId != null) {
                FindRoomResponse response = (FindRoomResponse) AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(
                        FindRoomRequest.newBuilder()
                                .setRoomId(Long.valueOf(roomId.toStringUtf8()))
                                .build()
                );
                if (response.getExists()) {
                    break;
                }
            }
            // 房间不存在
            ReplyUtils.reply(ctx, DOMAIN.GATEWAY, ERROR_CODE.ROOM_NOT_FOUND_VALUE, messageBody);
            return false;
        } while (false);

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());

        if (userId <= 0) {
            // 用户未登录
            ReplyUtils.reply(ctx, DOMAIN.GATEWAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

        AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(
                ChangeAccesssDoamin.newBuilder()
                        .setUserId(userId)
                        .setDomain(DOMAIN.GAME_ROOM)
                        .build()
        );

        return true;
    }

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.GAME_ROOM;
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
