package com.game.room.portal;

import akka.actor.ActorRef;
import com.framework.akka.AbstractRouterActor;
import com.framework.akka.ApiRequestWithActor;
import com.framework.message.BusinessMessage;
import com.framework.message.RequestArg;
import com.framework.message.ResponseMessage;
import com.framework.message.ServerRouterMessage;
import com.game.room.MessageProvider;
import com.game.room.proto.ErrorCodeDef;
import com.game.room.service.RoomManger;
import com.game.room.service.RoomRouterEntity;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomRouter extends AbstractRouterActor {

    private final ActorRef roomManger;

    public RoomRouter() {
        roomManger = getContext().actorOf(RoomManger.props(), "roomManger");
    }

    @Override
    public void onRequest(ApiRequestWithActor request) {
        // 服务端请求
        if (!request.isServer()) {
            clientRequest(request);
            return;
        }
        // 服务端直接的请求
        serverRequest(request);
    }

    public static ResponseMessage NotFoundMessage(Internal.EnumLite apiOpcode) {
        return ResponseMessage.newMessage(apiOpcode, MessageProvider.produceMessage(BusinessMessage.error(ErrorCodeDef.ERROR_CODE.ROOM_NOT_FOUND)));
    }

    private void serverRequest(ApiRequestWithActor request) {
        roomManger.forward(ServerRouterMessage.newServerRouterMessage(request), getContext());
    }

    private void clientRequest(ApiRequestWithActor request) {
        // 客户端请求
        RequestArg roomIdArg = request.getArg(0);
        if (roomIdArg != null) {
            long roomId = roomIdArg.getAsLong();
            if (roomId > 0) {
                RoomRouterEntity roomRouterEntity = new RoomRouterEntity();
                roomRouterEntity.setRoomId(roomId);
                ApiRequestWithActor apiRequestWithActor = ApiRequestWithActor.newClientApiRequestWithActor(
                        request.getUserId(),
                        request.getApiOpcode(),
                        request.getVersion(),
                        request.subArg(1)
                );
                roomRouterEntity.setApiRequestWithActor(apiRequestWithActor);
                // 重定向到
                roomManger.forward(roomRouterEntity, getContext());
                return;
            }
        }
        getSelf().tell(NotFoundMessage(request.getApiOpcode()), getSelf());
    }

    //      [
//          0 => [ 0 => {},1 => {}, 2 => {} ],
//          1 => [ 0 => {},1 => {}, 2 => {} ],
//          2 => [ 0 => {},1 => {}, 2 => {} ],
//      ]
}
