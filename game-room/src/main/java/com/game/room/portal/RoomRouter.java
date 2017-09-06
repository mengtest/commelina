package com.game.room.portal;

import akka.actor.ActorRef;
import com.framework.akka.AbstractRouterActor;
import com.framework.akka.ApiRequestWithActor;
import com.framework.message.BusinessMessage;
import com.framework.message.RequestArg;
import com.framework.message.ResponseMessage;
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
        RequestArg roomIdArg = request.getArg(0);
        if (roomIdArg != null) {
            long roomId = roomIdArg.getAsLong();
            if (roomId > 0) {
                RoomRouterEntity roomRouterEntity = new RoomRouterEntity();
                roomRouterEntity.setRoomId(roomId);
                roomRouterEntity.setApiRequestWithActor(
                        new ApiRequestWithActor(request.getUserId(), request.getApiOpcode(), request.getVersion(), request.subArg(1)));
                // 重定向到
                roomManger.forward(roomRouterEntity, getContext());
                return;
            }
        }
        getSelf().tell(NotFoundMessage(request.getApiOpcode()), getSelf());
    }

    public static ResponseMessage NotFoundMessage(Internal.EnumLite apiOpcode) {
        return ResponseMessage.newMessage(apiOpcode, MessageProvider.produceMessage(BusinessMessage.error(ErrorCodeDef.ERROR_CODE.ROOM_NOT_FOUND)));
    }

    //      [
//          0 => [ 0 => {},1 => {}, 2 => {} ],
//          1 => [ 0 => {},1 => {}, 2 => {} ],
//          2 => [ 0 => {},1 => {}, 2 => {} ],
//      ]
}
