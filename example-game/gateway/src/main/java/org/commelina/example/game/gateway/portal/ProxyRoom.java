package org.commelina.example.game.gateway.portal;

import org.commelina.akka.dispatching.DefaultClusterActorRequestHandler;
import org.commelina.akka.dispatching.cluster.RouterFrontedClusterActor;
import org.commelina.akka.dispatching.local.AkkaLocalWorkerSystem;
import org.commelina.akka.dispatching.proto.ApiRequest;
import org.commelina.akka.dispatching.proto.ApiRequestForward;
import org.commelina.core.BusinessMessage;
import org.commelina.core.DefaultMessageProvider;
import org.commelina.core.MessageBody;
import org.commelina.example.game.common.proto.DOMAIN;
import org.commelina.example.game.gateway.proto.ERROR_CODE;
import org.commelina.niosocket.ContextAdapter;
import org.commelina.niosocket.NioSocketRouter;
import org.commelina.niosocket.ReplyUtils;
import org.commelina.niosocket.proto.SocketASK;
import com.game.gateway.proto.ChangeAccesssDoamin;
import com.game.gateway.proto.FindRoomRequest;
import com.game.gateway.proto.FindRoomResponse;
import com.google.protobuf.ByteString;
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
