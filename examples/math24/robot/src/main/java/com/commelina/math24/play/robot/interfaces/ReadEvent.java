package com.commelina.math24.play.robot.interfaces;

import com.commelina.math24.play.robot.niosocket.MemberEventLoop;
import com.commelina.niosocket.proto.SocketMessage;

/**
 * @author @panyao
 * @date 2017/9/11
 */
public interface ReadEvent extends Identify {

    /**
     * 服务端响应事件
     *
     * @param eventLoop
     * @param msg
     * @return
     */
    default EventResult onMessage(MemberEventLoop eventLoop, SocketMessage msg) {
        throw new UnsupportedOperationException();
    }

    /**
     * 如果不再使用，则返回 remove 则会不会再在事件循环内了
     */
    enum EventResult {
        REMOVE, UN_REMOVE, ADD_HISTORY
    }

}
