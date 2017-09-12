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

    public void addEvent(ReadEvent event) {
        readEvents.add(event);
    }

    public void addEvent(HandlerEvent event) {
        eventLoop.execute(() -> event.handle(this, context));
    }

    public void addEvent(MemberEvent event) {
        // 注册默认回调
        addEvent((ReadEvent)event);
        eventLoop.execute(() -> event.handle(this, context));
    }

    public void removeReadEvent(Class<? extends ReadEvent> readEvent) {
        eventLoop.execute(() -> {
            Iterator<ReadEvent> readEventIterator = readEvents.iterator();
            while (readEventIterator.hasNext()) {
                ReadEvent event = readEventIterator.next();
                if (event.getClass().equals(readEvent)) {
                    readEventIterator.remove();
                    break;
                }
            }
        });
    }

    void executeRequest(ChannelHandlerContext ctx, SocketMessage msg) {
        eventLoop.execute(() -> {
            Iterator<ReadEvent> readEventIterator = readEvents.iterator();
            while (readEventIterator.hasNext()) {
                ReadEvent event = readEventIterator.next();
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
                        readEventIterator.remove();
                        break;
                }
            }
        });
    }

}
