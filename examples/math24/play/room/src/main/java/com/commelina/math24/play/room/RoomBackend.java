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
import com.google.protobuf.ByteString;
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

        Long roomId = usersToRoomId.get(onlineEvent.getLoginUserId());

        if (roomId == null || roomId <= 0) {
            getLogger().info("User id {} not found room id.", onlineEvent.getLoginUserId());
            return;
        }

        ActorRef roomContext = roomIdToRoomContextActor.get(roomId);
        if (roomContext == null) {
            getLogger().info("RoomContext id {} not instance RoomContext.", roomId);
            return;
        }

        roomContext.forward(onlineEvent, getContext());
    }

    @Override
    public void onOffline(MemberOfflineEvent offlineEvent) {
        Long roomId = usersToRoomId.get(offlineEvent.getLogoutUserId());

        if (roomId == null || roomId <= 0) {
            getLogger().info("User id {} not found room id.", offlineEvent.getLogoutUserId());
            return;
        }

        ActorRef roomContext = roomIdToRoomContextActor.get(roomId);
        if (roomContext == null) {
            getLogger().info("RoomContext id {} not instance RoomContext.", roomId);
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
        ByteString roomIdArg = request.getArgs(0);
        if (roomIdArg == null) {
            response(StaticProtoBuffDefined.ROOM_NOT_FOUND);
            return;
        }

        final long roomId = Long.valueOf(roomIdArg.toStringUtf8());
        if (roomId <= 0) {
            response(StaticProtoBuffDefined.ROOM_NOT_FOUND);
            return;
        }

        ActorRef roomContext = roomIdToRoomContextActor.get(roomId);
        if (roomContext == null) {
            response(StaticProtoBuffDefined.ROOM_NOT_FOUND);
            getLogger().info("Api request room id {} not instance RoomContext.", roomId);
            return;
        }

        // 移除第一个元素
        request.getArgsList().remove(0);

        roomContext.forward(request, getContext());
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

}