package com.commelina.math24.play.gateway.portal;

import com.commelina.akka.DefaultClusterActorRequestHandler;
import com.commelina.akka.dispatching.RouterClusterFrontendActor;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.commelina.core.BusinessMessage;
import com.commelina.core.DefaultMessageProvider;
import com.commelina.core.MessageBody;
import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.gateway.proto.ERROR_CODE;
import com.commelina.niosocket.ContextAdapter;
import com.commelina.niosocket.NioSocketRouter;
import com.commelina.niosocket.ReplyUtils;
import com.commelina.niosocket.proto.SocketASK;
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
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {
//        do {
//            ByteString roomId = ask.getArgs(0);
//            if (roomId != null) {
//                FindRoomResponse response = (FindRoomResponse) AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(
//                        FindRoomRequest.newBuilder()
//                               .setRoomId(Long.valueOf(roomId.toStringUtf8()))
//                                .build()
//                );
//                if (response.getExists()) {
//                    break;
//                }
//            }
//            // 房间不存在
//            ReplyUtils.reply(ctx, DOMAIN.GATEWAY, ERROR_CODE.ROOM_NOT_FOUND_VALUE, messageBody);
//            return false;
//        } while (false);

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());

        if (userId <= 0) {
            // 用户未登录
            ReplyUtils.reply(ctx, DOMAIN.GATEWAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

//        AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(
//                ChangeAccesssDoamin.newBuilder()
//                        .setUserId(userId)
//                        .setDomain(DOMAIN.GAME_ROOM)
//                        .build()
//        );

        return true;
    }

    @Override
    public Internal.EnumLite getRouterId() {
        return DOMAIN.GAME_ROOM;
    }

    public static class RoomRouterClusterFrontendActor extends RouterClusterFrontendActor {

        public RoomRouterClusterFrontendActor(Internal.EnumLite myRouterId) {
            super(myRouterId);
        }

        @Override
        public int selectActor(ApiRequest request) {
            return (int) (Long.valueOf(request.getArgs(0).toStringUtf8()) % 2);
        }

        @Override
        public int selectActor(ApiRequestForward forward) {
            return super.selectActor(forward);
        }

    }

}
