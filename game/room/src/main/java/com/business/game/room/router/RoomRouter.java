package com.business.game.room.router;

import akka.actor.ActorRef;
import com.business.game.message.matching_room.proto.MATCHING_ROOM_METHODS;
import com.business.game.room.entity.PlayerStatus;
import com.business.game.room.event.PlayerStatusEvent;
import com.business.game.room.service.RoomManger;
import com.game.room.proto.ERROR_CODE;
import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.BackendActor;
import com.github.freedompy.commelina.akka.dispatching.proto.ApiRequest;
import com.github.freedompy.commelina.akka.dispatching.proto.ApiRequestForward;
import com.github.freedompy.commelina.core.BusinessMessage;
import com.github.freedompy.commelina.core.DefaultMessageProvider;
import com.github.freedompy.commelina.core.MessageBody;
import com.google.protobuf.ByteString;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author @panyao
 * @date 2017/9/26
 */
public class RoomRouter extends BackendActor {

    private final ActorRef roomManger = getContext().getSystem().actorOf(RoomManger.props(), "roomManger");

    private static final MessageBody ROOM_NOT_FOUND =
            DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_NOT_FOUND));

    @Override
    public void onOffline(long logoutUserId) {
        // 用户下线，标记为下线
        roomManger.tell(new PlayerStatusEvent(logoutUserId, PlayerStatus.Offline), getSelf());
    }

    @Override
    public void onOnline(long logoutUserId) {
        // 用户上线,标记为重新上线
        roomManger.tell(new PlayerStatusEvent(logoutUserId, PlayerStatus.Online), getSelf());
    }

    @Override
    public void onRequest(ApiRequest request) {
        ByteString roomIdArg = request.getArgs(0);
        if (roomIdArg == null) {
            response(ROOM_NOT_FOUND);
            return;
        }

        final long roomId = Long.valueOf(roomIdArg.toStringUtf8());
        if (roomId <= 0) {
            response(ROOM_NOT_FOUND);
            return;
        }

        // 检查房间是否存在

        // 重定向
        requestRouter(request);
    }

    @Override
    public void onForward(ApiRequestForward forward) {
        switch (forward.getOpcode()) {
            case MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:
                List<Long> userIds = forward.getArgsList()
                        .stream()
                        .map(v -> Long.valueOf(v.toStringUtf8()))
                        .collect(Collectors.toList());

                roomManger.forward(new RoomManger.CreateRoomEntity(userIds), getContext());
                break;
            default:
                unhandled(forward);
        }
    }

    private void requestRouter(ApiRequest request) {

    }
}