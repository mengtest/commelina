package com.game.room.router_v3;

import akka.actor.ActorRef;
import com.framework.akka_router.cluster.node.ClusterChildNodeBackedActor;
import com.framework.message.*;
import com.game.room.entity.PlayerEntity;
import com.game.room.entity.PlayerStatus;
import com.game.room.event.PlayerStatusEvent;
import com.game.room.proto.ERROR_CODE;
import com.game.room.service.RoomManger;
import com.google.common.collect.Lists;
import com.google.protobuf.Internal;
import com.message.matching_room.proto.MATCHING_ROOM_METHODS;

import java.util.List;

/**
 * Created by @panyao on 2017/9/26.
 */
public class RoomRouter extends ClusterChildNodeBackedActor {

    private final ActorRef roomManger = getContext().getSystem().actorOf(RoomManger.props(), "roomManger");

    private static final MessageBus ROOM_NOT_FOUND = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_NOT_FOUND));

    @Override
    public Internal.EnumLite getRouterId() {
        // 这里就是配置节点，节点 1 节点 2 节点 3，不会因为集群的启动顺序而改变
        return () -> 0;
    }

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

        RequestArg roomIdArg = request.getArg(0);
        if (roomIdArg == null) {
            response(ROOM_NOT_FOUND);
            return;
        }

        final long roomId = roomIdArg.getAsLong();
        if (roomId <= 0) {
            response(ROOM_NOT_FOUND);
            return;
        }

        // 检查房间是否存在

        // 重定向
    }

    @Override
    public void onForward(ApiRequestForward forward) {
        switch (forward.getOpcode().getNumber()) {
            case MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:
                long[] userIds = forward.getLongArgs();

                // 加载用户信息
                final List<PlayerEntity> playerEntities = Lists.newArrayListWithExpectedSize(forward.getArgs().length);
                final RoomManger.CreateRoomEntity createRoomEntity = new RoomManger.CreateRoomEntity();
                createRoomEntity.setPlayers(playerEntities);
                roomManger.forward(playerEntities, getContext());
                break;
        }
    }
}