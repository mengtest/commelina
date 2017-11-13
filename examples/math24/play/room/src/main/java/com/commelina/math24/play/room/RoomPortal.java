package com.commelina.math24.play.room;

import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.commelina.akka.dispatching.cluster.nodes.AbstractBackendActor;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.akka.dispatching.proto.ApiRequestForward;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.core.BusinessMessage;
import com.commelina.core.DefaultMessageProvider;
import com.commelina.core.MessageBody;
import com.commelina.math24.matching_room.proto.MATCHING_ROOM_METHODS;
import com.commelina.math24.play.room.context.RoomContext;
import com.commelina.math24.play.room.proto.ERROR_CODE;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 房间 server 入口
 *
 * @author @panyao
 * @date 2017/9/26
 */
public class RoomPortal extends AbstractBackendActor {

    /**
     * roomId -> RoomContextActorRef
     */
    private final BiMap<Long, ActorRef> roomIdToRoomContextActor = HashBiMap.create(128);

    /**
     * userId -> roomId
     */
    private final Map<Long, Long> usersToRoomId = Maps.newHashMap();

    /**
     * 当前 自增的房间id
     */
    private long currentRoomId = 0;

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);

    /**
     * 房间不存在通知信息
     */
    private static final MessageBody ROOM_NOT_FOUND =
            DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.ROOM_NOT_FOUND));

    @Override
    public void onOnline(MemberOnlineEvent onlineEvent) {

        Long roomId = usersToRoomId.get(onlineEvent.getLoginUserId());

        if (roomId == null || roomId <= 0) {
            logger.info("User id {} not found room id.", onlineEvent.getLoginUserId());
            return;
        }

        ActorRef roomContext = roomIdToRoomContextActor.get(roomId);
        if (roomContext == null) {
            logger.info("RoomContext id {} not instance RoomContext.", roomId);
            return;
        }

        roomContext.forward(onlineEvent, getContext());
    }

    @Override
    public void onOffline(MemberOfflineEvent offlineEvent) {
        Long roomId = usersToRoomId.get(offlineEvent.getLogoutUserId());

        if (roomId == null || roomId <= 0) {
            logger.info("User id {} not found room id.", offlineEvent.getLogoutUserId());
            return;
        }

        ActorRef roomContext = roomIdToRoomContextActor.get(roomId);
        if (roomContext == null) {
            logger.info("RoomContext id {} not instance RoomContext.", roomId);
            return;
        }

        roomContext.forward(offlineEvent, getContext());
    }
//    @Override
//    public void onOffline(long logoutUserId) {
//        // 用户下线，标记为下线
//        sendPlayerStatus(new PlayerStatusEvent(logoutUserId, PlayerStatus.Offline));
//    }
//
//    @Override
//    public void onOnline(long logoutUserId) {
//        // 用户上线,标记为重新上线
//        sendPlayerStatus(new PlayerStatusEvent(logoutUserId, PlayerStatus.Online));
//    }

    /**
     * 当客户端有请求过来时触发
     *
     * @param request
     */
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

        ActorRef roomContext = roomIdToRoomContextActor.get(roomId);
        if (roomContext == null) {
            response(ROOM_NOT_FOUND);
            logger.info("Api request room id {} not instance RoomContext.", roomId);
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
            case MATCHING_ROOM_METHODS.CREATE_ROOM_VALUE:
                createRoom(forward);
                break;
            default:

        }
    }

    private void createRoom(ApiRequestForward forward) {
        List<Long> userIds = forward.getArgsList()
                .stream()
                .map(v -> Long.valueOf(v.toStringUtf8()))
                .collect(Collectors.toList());

        // fixme 加载用户信息
        // 默认用户为在线

        final long newRoomId = currentRoomId++;
        final ActorRef roomContext = getContext().actorOf(RoomContext.props(newRoomId, null), "roomContext");
        roomIdToRoomContextActor.put(newRoomId, roomContext);

        // 把当前用户加入 room Id 列表上
        userIds.forEach(v -> usersToRoomId.put(v, newRoomId));
    }

}