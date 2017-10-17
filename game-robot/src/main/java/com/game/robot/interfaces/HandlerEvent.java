package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketASK;

/**
 *
 * @author @panyao
 * @date 2017/9/11
 */
public interface HandlerEvent {

    SocketASK handler(MemberEventLoop eventLoop);

}
