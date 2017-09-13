package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketMessage;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by @panyao on 2017/9/11.
 */
public final class MemberEventLoop {

    private final List<ReadEvent> readEvents = Lists.newArrayList();
    ChannelHandlerContext context;
    final EventLoop eventLoop = new DefaultEventLoop();

    public void addEvent(ReadEvent event, ReadEvent... events) {
        readEvents.add(event);
        Collections.addAll(readEvents, events);
    }

    public void addEvent(HandlerEvent event, HandlerEvent... events) {
        eventLoop.execute(() -> event.handle(this, context));
        for (final HandlerEvent handlerEvent : events) {
            eventLoop.execute(() -> handlerEvent.handle(this, context));
        }
    }

    public void addEvent(MemberEvent event, MemberEvent... events) {
        // 注册默认回调
        addEvent((ReadEvent) event);
        eventLoop.execute(() -> event.handle(this, context));

        for (final MemberEvent memberEvent : events) {
            addEvent((ReadEvent) memberEvent);
            eventLoop.execute(() -> memberEvent.handle(this, context));
        }
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

    void acceptor(ChannelHandlerContext ctx, SocketMessage msg) {
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
