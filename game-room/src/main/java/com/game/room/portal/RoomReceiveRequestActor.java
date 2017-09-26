package com.game.room.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.framework.akka.AbstractReceiveRequestActor;
import com.framework.message.ApiRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.RequestArg;
import com.framework.message.ResponseMessage;
import com.game.room.MessageProvider;
import com.game.room.proto.ERROR_CODE;
import com.game.room.service.RoomClientRouterEntity;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomReceiveRequestActor extends AbstractReceiveRequestActor {

    private final ActorRef roomManger;

    public RoomReceiveRequestActor(ActorRef roomManger) {
        this.roomManger = roomManger;
    }

    @Override
    public void onRequest(ApiRequest request) {
        // 客户端请求
        RequestArg roomIdArg = request.getArg(0);
        if (roomIdArg != null) {
            long roomId = roomIdArg.getAsLong();
            if (roomId > 0) {
                RoomClientRouterEntity roomClientRouterEntity = new RoomClientRouterEntity();
                roomClientRouterEntity.setRoomId(roomId);
                ApiRequest apiRequest = ApiRequest.newClientApiRequestWithActor(
                        request.getUserId(),
                        request.getOpcode(),
                        request.getVersion(),
                        request.subArg(1)
                );
                roomClientRouterEntity.setApiRequest(apiRequest);
                // 重定向到
                roomManger.forward(roomClientRouterEntity, getContext());
                return;
            }
        }
        getSelf().tell(NotFoundMessage(request.getOpcode()), getSelf());
    }

    public static ResponseMessage NotFoundMessage(Internal.EnumLite apiOpcode) {
        return ResponseMessage.newMessage(apiOpcode, MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_NOT_FOUND)));
    }

    public static Props props(ActorRef roomManger) {
        return Props.create(RoomReceiveRequestActor.class, roomManger);
    }

//      [
//          0 => [ 0 => {},1 => {}, 2 => {} ],
//          1 => [ 0 => {},1 => {}, 2 => {} ],
//          2 => [ 0 => {},1 => {}, 2 => {} ],
//      ]
}
