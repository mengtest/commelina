package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/11.
 */
public interface InputEvent {

    boolean isReadMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode);

    EventResult channelRead(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg);

    // 如果不再使用，则返回 remove 则会不会再在事件循环内了
    enum EventResult {
        NONE, REMOVE
    }

}
