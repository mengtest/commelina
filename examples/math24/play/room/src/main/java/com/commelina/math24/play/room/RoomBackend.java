package com.commelina.math24.play.room;

import akka.actor.ActorRef;
import com.commelina.akka.dispatching.nodes.AbstractBackendActor;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.math24.matching_room.proto.CREATE_ROOM_REQUEST;
import com.commelina.math24.matching_room.proto.MATCH_ROOM_REQUEST_OPCODE;
import com.commelina.math24.play.room.context.RoomContext;
import com.google.common.collect.Maps;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Map;

/**
 * 房间 server 入口
 *
 * @author @panyao
 * @date 2017/9/26
 */
public class RoomBackend extends AbstractBackendActor {

    /**
     * roomId -> RoomContextActorRef
     */
    private final Map<Long, ActorRef> roomIdToRoomContextActor = Maps.newTreeMap();

    /**
     * userId -> roomId
     */
    private final Map<Long, Long> usersToRoomId = Maps.newTreeMap();

    @Override
    public void onOnline(MemberOnlineEvent onlineEvent) {
        ActorRef roomContext = selectRoomContext(onlineEvent.getLoginUserId());
        if (roomContext == null) {
            getLogger().info("User id {} not instance RoomContext.", onlineEvent.getLoginUserId());
            return;
        }
        roomContext.forward(onlineEvent, getContext());
    }

    @Override
    public void onOffline(MemberOfflineEvent offlineEvent) {
        ActorRef roomContext = selectRoomContext(offlineEvent.getLogoutUserId());
        if (roomContext == null) {
            getLogger().info("User id {} not instance RoomContext.", offlineEvent.getLogoutUserId());
            return;
        }
        roomContext.forward(offlineEvent, getContext());
    }

    /**
     * 当客户端有请求过来时触发
     *
     * @param request
     */
    @Override
    public void onRequest(ApiRequest request) {
        ActorRef roomContext = selectRoomContext(request.getLoginUserId());
        if (roomContext == null) {
            response(StaticProtoBuffDefined.ROOM_NOT_FOUND);
            getLogger().info("User id {} not instance RoomContext.", request.getLoginUserId());
            return;
        }
        switchRequest(request);
    }

    /**
     * 当有服务端消息过来时触发
     *
     * @param forward
     */
    @Override
    public void onForward(ApiRequestForward forward) {
        switch (forward.getOpcode()) {
            case MATCH_ROOM_REQUEST_OPCODE.CREATE_ROOM_VALUE:
                createRoom(forward);
                break;
            default:
                unhandled(forward);
        }
    }

    private void createRoom(ApiRequestForward forward) {
        CREATE_ROOM_REQUEST request;
        try {
            request = CREATE_ROOM_REQUEST.parseFrom(forward.getArgs(0));
        } catch (InvalidProtocolBufferException e) {
            getLogger().error("Input args error {}", e);
            return;
        }

        // fixme 加载用户信息
        // 默认用户为在线
        final ActorRef roomContext = getContext().actorOf(RoomContext.props(request.getRoomId(), null), "roomContext");
        roomIdToRoomContextActor.put(request.getRoomId(), roomContext);

        // 把当前用户加入 room Id 列表上
        request.getUserIdsList().forEach(u -> usersToRoomId.put(u, request.getRoomId()));
    }

    private ActorRef selectRoomContext(long userId) {
        Long roomId = usersToRoomId.get(userId);
        if (roomId == null || roomId <= 0) {
            getLogger().info("User id {} not found room id.", userId);
            return null;
        }
        return roomIdToRoomContextActor.get(roomId);
    }

    private void switchRequest(ApiRequest request) {

    }

}