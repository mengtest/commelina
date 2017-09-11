package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;

import java.util.Iterator;
import java.util.List;

/**
 * Created by @panyao on 2017/9/11.
 */
public class MemberEventLoop {

    List<InputEvent> inputEvents;
    ChannelHandlerContext context;
    final EventLoop eventLoop = new DefaultEventLoop();

    public void addInputEvent(InputEvent event) {
        inputEvents.add(event);
    }

    public void executeMemberEvent(OutputEvent event) {
        // 注册默认回调
        if (event instanceof InputEvent) {
            addInputEvent((InputEvent) event);
        }
        eventLoop.execute(() -> event.member(this, context));
    }

    void executeRequest(ChannelHandlerContext ctx, SocketMessage msg) {
        eventLoop.execute(() -> {
            Iterator<InputEvent> inputEventIterator = inputEvents.iterator();
            while (inputEventIterator.hasNext()) {
                InputEvent event = inputEventIterator.next();
                if (!event.isReadMe(() -> msg.getDomain(), () -> msg.getOpcode())) {
                    continue;
                }
                switch (event.channelRead(this, ctx, msg)) {
                    case REMOVE:
                        inputEventIterator.remove();
                        break;
                }
            }
        });
    }

}
