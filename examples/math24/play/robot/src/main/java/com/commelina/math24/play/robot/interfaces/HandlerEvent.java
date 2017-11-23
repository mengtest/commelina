package com.commelina.math24.play.robot.interfaces;

import com.commelina.math24.play.robot.niosocket.MemberEventLoop;
import com.commelina.niosocket.proto.SocketASK;

/**
 * 事件 hander
 *
 * @author @panyao
 * @date 2017/9/11
 */
public interface HandlerEvent {

    /**
     * 事件创建的时候需要向 server 发送的操作
     *
     * @param eventLoop
     * @return
     */
    SocketASK onCreatedAsk(MemberEventLoop eventLoop);

}
