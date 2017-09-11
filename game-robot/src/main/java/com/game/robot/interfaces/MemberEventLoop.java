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
public final class MemberEventLoop {

    List<ReadEvent> readEvents;
    ChannelHandlerContext context;
    final EventLoop eventLoop = new DefaultEventLoop();

    public void addInputEvent(ReadEvent event) {
        readEvents.add(event);
    }

    public void executeMemberEvent(HandlerEvent event) {
        // 注册默认回调
        if (event instanceof ReadEvent) {
            addInputEvent((ReadEvent) event);
        }
        eventLoop.execute(() -> event.handle(this, context));
    }

    public void executeMemberEvent(MemberEvent event) {
        // 注册默认回调
        addInputEvent(event);
        eventLoop.execute(() -> event.handle(this, context));
    }

    void executeRequest(ChannelHandlerContext ctx, SocketMessage msg) {
        eventLoop.execute(() -> {
            Iterator<ReadEvent> inputEventIterator = readEvents.iterator();
            while (inputEventIterator.hasNext()) {
                ReadEvent event = inputEventIterator.next();
                if (!event.isMe(() -> msg.getDomain(), () -> msg.getOpcode())) {
                    continue;
                }
                switch (event.read(this, ctx, msg)) {
                    case UN_REMOVE:
                        break;
                    case ADD_HISTORY:
                        // FIXME: 2017/9/11 还没有实现
                        break;
                    case REMOVE:
                    default:
                        inputEventIterator.remove();
                        break;


                }
            }
        });
    }

}
