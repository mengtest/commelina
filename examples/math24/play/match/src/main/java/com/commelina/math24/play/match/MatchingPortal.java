package com.commelina.math24.play.match;

import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.commelina.akka.dispatching.cluster.nodes.AbstractBackendActor;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.math24.play.match.mode.GlobalMatch;
import com.commelina.math24.play.match.proto.MATCH_METHODS;
import com.commelina.math24.play.match.proto.MATCH_MODE;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author @panyao
 * @date 2017/9/26
 */
public class MatchingPortal extends AbstractBackendActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().getSystem(), this);

    /**
     * 用户访问的最后一个 mode
     */
    private final Map<Long, MATCH_MODE> userLastAccessMode = Maps.newHashMap();

    /**
     * 房间管理器
     */
    private ActorRef roomManger;

    /**
     * 10 人局游戏
     */
    private ActorRef globalMatchActorRef = getContext().getSystem().actorOf(GlobalMatch.props(10));

    @Override
    public void preStart() {
        super.preStart();

    }

    @Override
    public void onOffline(long logoutUserId) {
        MATCH_MODE mode = userLastAccessMode.get(logoutUserId);
        if (mode != null) {
            selectMatchModeActorRef(mode).tell(null, null);
            // 移除用户最后的访问记录
            userLastAccessMode.remove(logoutUserId);
        }
        // 用户下线，取消匹配
        // match.forward(new Matching.CancelMatch(logoutUserId, () -> 0), getContext());
    }

    @Override
    public void onRequest(ApiRequest request) {
        switch (request.getOpcode()) {
            // 加入匹配队列
            case MATCH_METHODS.JOIN_MATCH_QUENE_VALUE:
//                matching.forward(new Matching.JoinMatch(request.getUserId(), request.getOpcode()), getContext());
                break;
            // 用户取消匹配
//            case MATCH_METHODS.CANCEL_MATCH_QUENE_VALUE:
//                matching.forward(new Matching.JoinMatch(request.getUserId(), request.getOpcode()), getContext());
//                break;
        }
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