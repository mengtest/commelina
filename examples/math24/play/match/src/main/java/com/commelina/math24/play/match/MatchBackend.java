package com.commelina.math24.play.match;

import akka.actor.ActorRef;
import com.commelina.akka.dispatching.nodes.AbstractBackendActor;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.math24.play.match.mode.GlobalMatch;
import com.commelina.math24.play.match.proto.*;
import com.commelina.math24.play.match.room.RoomManger;
import com.google.protobuf.ByteString;

/**
 * @author @panyao
 * @date 2017/9/26
 */
public class MatchBackend extends AbstractBackendActor {

    /**
     * 全局匹配
     */
    private ActorRef globalMatchActorRef;

    /**
     * 房间管理器
     */
    private ActorRef roomManger;

    @Override
    public void preStart() {
        super.preStart();
        globalMatchActorRef = getContext().getSystem().actorOf(GlobalMatch.props(10), AbstractMatchServiceActor.GLOBAL_MATCH);
        roomManger = getContext().getSystem().actorOf(RoomManger.props(), AbstractMatchServiceActor.ROOM_MANAGER);
    }

    @Override
    public void onOffline(MemberOfflineEvent offlineEvent) {
        // 向匹配和房间广播用户离线事件
        globalMatchActorRef.tell(offlineEvent, getSelf());
        roomManger.tell(offlineEvent, getSelf());
    }

    @Override
    public void onOnline(MemberOnlineEvent onlineEvent) {
        // 向临时房间广播用户上线事件
        roomManger.tell(onlineEvent, getSelf());
    }

    @Override
    public void onRequest(ApiRequest request) {
        switch (request.getOpcode()) {
            // 加入匹配
            case REQUEST_OPCODE.JOIN_MATCH_QUENE_VALUE:
                // 发送一个异步消息到匹配队列
                switchMatchModeActor(request).tell(JoinMatch.newBuilder()
                        .setUserId(request.getLoginUserId())
                        .build(), getSelf());
                // 回复客户端成功
                emptyResponse();
                break;
            // 取消匹配
            case REQUEST_OPCODE.EXIT_MATCH_QUENE_VALUE:
                switchMatchModeActor(request).tell(CancelMatch.newBuilder()
                        .setUserId(request.getLoginUserId())
                        .build(), getSelf());
                // 回复客户端空消息
                emptyResponse();
                break;
            // 加入临时房间
            case REQUEST_OPCODE.JOIN_TEMPORARY_ROOM_VALUE:
                roomManger.tell(JoinTemporaryRoom.newBuilder()
                        .setUserId(request.getLoginUserId())
                        .build(), getSelf());
                // 回复客户端成功
                emptyResponse();
                break;
            default:
                unhandled(request);
        }
    }

    private ActorRef switchMatchModeActor(ApiRequest request) {
        ByteString matchMode = request.getArgs(0);
        if (matchMode == null || matchMode.isEmpty()) {
            throw new IllegalArgumentException("Arg : matchMode must be input.");
        }

        return selectMatchModeActorRef(MATCH_MODE.forNumber(Integer.valueOf(matchMode.toStringUtf8())));
    }

    private ActorRef selectMatchModeActorRef(MATCH_MODE mode) {
        switch (mode) {
            case FAST:
                return globalMatchActorRef;
            default:
                throw new IllegalArgumentException();
        }
    }

}