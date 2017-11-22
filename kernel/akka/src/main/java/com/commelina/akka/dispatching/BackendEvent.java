package com.commelina.akka.dispatching;

import com.commelina.akka.dispatching.proto.ActorBroadcast;
import com.commelina.akka.dispatching.proto.ActorNotify;
import com.commelina.akka.dispatching.proto.ActorWorld;

/**
 * @author panyao
 * @date 2017/11/16
 */
public interface BackendEvent {

    /**
     * 通知事件
     *
     * @param notify
     */
    void event(ActorNotify notify);

    /**
     * 广播事件
     *
     * @param broadcast
     */
    void event(ActorBroadcast broadcast);

    /**
     * 在线所有人广播事件
     *
     * @param world
     */
    void event(ActorWorld world);

}
