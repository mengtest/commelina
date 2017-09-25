package com.framework.akka;

import com.framework.message.BroadcastMessage;
import com.framework.message.NotifyMessage;
import com.framework.message.WorldMessage;

/**
 * Created by @panyao on 2017/9/15.
 * <p>
 * 用于 server 互相发送消息,这边是客户端模块
 */
 interface ActorRemoteHandler extends ActorMemberEvent {

    /**
     * 回复消息到客户端
     * @param message
     */
    void reply(NotifyMessage message);

    void reply(BroadcastMessage message);

    void reply(WorldMessage message);

}
