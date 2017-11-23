package com.commelina.math24.play.robot.niosocket;

import com.commelina.math24.play.robot.interfaces.HandlerEvent;
import com.commelina.math24.play.robot.interfaces.MemberEvent;
import com.commelina.math24.play.robot.interfaces.ReadEvent;
import com.commelina.niosocket.proto.SERVER_CODE;
import com.commelina.niosocket.proto.SocketASK;
import com.commelina.niosocket.proto.SocketMessage;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * @author @panyao
 * @date 2017/9/11
 */
public final class MemberEventLoop {

    private final Logger LOGGER = LoggerFactory.getLogger(MemberEventLoop.class);

    //    private final List<ReadEvent> readEvents = Lists.newCopyOnWriteArrayList();
    private final List<ReadEvent> readEvents = Lists.newArrayList();

    private final EventLoop eventLoop = new DefaultEventLoop();

    ChannelHandlerContext ctx;

    public void addEvent(ReadEvent event, ReadEvent... events) {
        readEvents.add(event);
        LOGGER.info("addEvent {}", event.getClass());
        Collections.addAll(readEvents, events);
        for (ReadEvent readEvent : events) {
            LOGGER.info("addEvent {}", readEvent.getClass());
        }
    }

    public void addEvent(HandlerEvent event, HandlerEvent... events) {
        eventLoop.execute(() -> ask(event.onCreatedAsk(this)));
        LOGGER.info("addEvent to execute task {}", event.getClass());
        for (final HandlerEvent handlerEvent : events) {
            eventLoop.execute(() -> ask(handlerEvent.onCreatedAsk(this)));
            LOGGER.info("addEvent to execute task {}", handlerEvent.getClass());
        }
    }

    public void addEvent(MemberEvent event, MemberEvent... events) {
        addEvent((ReadEvent) event);
        addEvent((HandlerEvent) event);

        for (MemberEvent memberEvent : events) {
            addEvent((ReadEvent) memberEvent);
            addEvent((HandlerEvent) memberEvent);
        }
    }

    public void removeReadEvent(Class<? extends ReadEvent> readEvent) {
        eventLoop.execute(() -> {
            while (readEvents.iterator().hasNext()) {
                ReadEvent event = readEvents.iterator().next();
                if (event.getClass().equals(readEvent)) {
                    readEvents.iterator().remove();
                    LOGGER.info("removeReadEvent {}", readEvent);
                    break;
                }
            }
        });
    }

    void acceptor(SocketMessage msg) {
        eventLoop.execute(() -> {
            while (readEvents.iterator().hasNext()) {
                ReadEvent event = readEvents.iterator().next();
                if (!event.tag(msg::getDomain, msg::getOpcode)) {
                    continue;
                }

                ReadEvent.EventResult result = msg.getCode() == SERVER_CODE.RESONSE_CODE
                        ? event.onResponse(this, msg) : event.onNotify(this, msg);

                switch (result) {
                    case UN_REMOVE:
                        break;
                    case ADD_HISTORY:
                        break;
                    case REMOVE:
                    default:
                        readEvents.iterator().remove();
                }

                return;
            }
            LOGGER.info("no handler execute {}", msg);
        });
    }

    void addSystemEvent() {

    }

    private void ask(SocketASK message) {
        if (ctx != null) {
            ctx.writeAndFlush(message);
            return;
        }

        while (errorTimes++ < ERROR_TIME_LIMIT) {
            if (ctx != null) {
                LOGGER.info("第{}次重连成功，消息已发送", errorTimes - 1);
                ctx.writeAndFlush(message);
                errorTimes = 0;
                return;
            }
            LOGGER.info("第{}次尝试重连,{}", errorTimes, LocalTime.now().withNano(0));
            // TODO: 2017/10/13 这里写得怪怪的，先偷懒了
            NettyClient.start(this);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                LOGGER.info("第{}次尝试重连错误,{}", errorTimes, e);
            }
        }
    }

    private int errorTimes = 0;
    static final int ERROR_TIME_LIMIT = 12;

}
