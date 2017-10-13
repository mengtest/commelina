package com.game.robot.interfaces;

import com.framework.niosocket.proto.SocketASK;
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
    ChannelHandlerContext ctx;
    final EventLoop eventLoop = new DefaultEventLoop();
    boolean isReady = false;

    public void addEvent(ReadEvent event, ReadEvent... events) {
        readEvents.add(event);
        Collections.addAll(readEvents, events);
    }

    public void addEvent(HandlerEvent event, HandlerEvent... events) {
        eventLoop.execute(() -> socketAsk(event.handler(this)));
        for (final HandlerEvent handlerEvent : events) {
            eventLoop.execute(() -> socketAsk(handlerEvent.handler(this)));
        }
    }

    public void addEvent(MemberEvent event, MemberEvent... events) {
        // 注册默认回调
        addEvent((ReadEvent) event);
        eventLoop.execute(() -> socketAsk(event.handler(this)));

        for (final MemberEvent memberEvent : events) {
            addEvent((ReadEvent) memberEvent);
            eventLoop.execute(() -> socketAsk(memberEvent.handler(this)));
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

    public void socketAsk(SocketASK ask) {
        if (!isReady) {
            while (true) {
                if (isReady) {
                    break;
                }
            }
        }
        ctx.writeAndFlush(ask);
    }

    void acceptor(ChannelHandlerContext ctx, SocketMessage msg) {
        eventLoop.execute(() -> {
            Iterator<ReadEvent> readEventIterator = readEvents.iterator();
            while (readEventIterator.hasNext()) {
                ReadEvent event = readEventIterator.next();

                if (!(event.getDomain().getNumber() == msg.getDomain() &&
                        event.getApiOpcode().getNumber() == msg.getOpcode())
                        ) {
                    continue;
                }
                event.read(this, msg);
                readEventIterator.remove();
            }
        });
    }

}
