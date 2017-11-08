package com.game.robot.interfaces;

import com.github.freedompy.commelina.niosocket.proto.SERVER_CODE;
import com.github.freedompy.commelina.niosocket.proto.SocketASK;
import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 *
 * @author @panyao
 * @date 2017/9/11
 */
public final class MemberEventLoop {

    private final Logger LOGGER = LoggerFactory.getLogger(MemberEventLoop.class);

    private final List<ReadEvent> readEvents = Lists.newCopyOnWriteArrayList();

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
        addEvent((HandlerEvent) event);

        for (final MemberEvent memberEvent : events) {
            addEvent((ReadEvent) memberEvent);
            addEvent((HandlerEvent) memberEvent);
        }
    }

    public void removeReadEvent(Class<? extends ReadEvent> readEvent) {
        eventLoop.execute(() -> {
            for (int i = 0; i < readEvents.size(); i++) {
                if (readEvents.get(i).getClass().equals(readEvent)) {
                    readEvents.remove(i);
                    break;
                }
            }

//            Iterator<ReadEvent> readEventIterator = readEvents.iterator();
//            while (readEventIterator.hasNext()) {
//                ReadEvent event = readEventIterator.next();
//                if (event.getClass().equals(readEvent)) {
//                    readEventIterator.remove();
//                    break;
//                }
//            }
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

    void acceptor(SocketMessage msg) {
        eventLoop.execute(() -> {

            for (int i = 0; i < readEvents.size(); i++) {
                ReadEvent event = readEvents.get(i);

                if (msg.getCode() == SERVER_CODE.RESONSE_CODE || msg.getCode() == SERVER_CODE.NOTIFY_CODE) {
                    if (!(event.getDomain().getNumber() == msg.getDomain() &&
                            event.getApiOpcode().getNumber() == msg.getOpcode()
                    )) {
                        continue;
                    }
                    switch (event.read(this, msg)) {
                        case UN_REMOVE:
                            break;
                        case ADD_HISTORY:
                            break;
                        case REMOVE:
                        default:
                            readEvents.remove(i);
                    }
                    break;
                } else {
                    switch (msg.getCode()) {
                        case RPC_API_NOT_FOUND:
                            LOGGER.error("Api 没有找到.forward:{},opcode:{}", msg.getDomain(), msg.getOpcode());
                            break;
                        case SERVER_ERROR:
                            LOGGER.error("服务器内部错误.");
                            break;
                        default:
                            LOGGER.error("未解析的SERVER_CODE.");

                    }
                    break;
                }
            }
//                Iterator < ReadEvent > readEventIterator = readEvents.iterator();
//        while (readEventIterator.hasNext()) {
//            ReadEvent event = readEventIterator.next();
//            if (!(
//                    event.getDomain().getNumber() == msg.getDomain() &&
//                            event.getApiOpcode().getNumber() == msg.getOpcode()
//            )) {
//                continue;
//            }
//            if (msg.getCode() == SERVER_CODE.RESONSE_CODE || msg.getCode() == SERVER_CODE.NOTIFY_CODE) {
//                switch (event.read(this, msg)) {
//                    case UN_REMOVE:
//                        break;
//                    case ADD_HISTORY:
//                        break;
//                    case REMOVE:
//                    default:
//                        readEventIterator.remove();
//                }
//            } else {
//                switch (msg.getCode()) {
//                    case RPC_API_NOT_FOUND:
//                        LOGGER.error("Api 没有找到.");
//                        break;
//                    case SERVER_ERROR:
//                        LOGGER.error("服务器内部错误.");
//                        break;
//                    default:
//                        LOGGER.error("未解析的SERVER_CODE.");
//
//                }
//            }
//            break;
//        }
        });
    }

}
