package com.commelina.math24.play.robot.niosocket;

import com.commelina.math24.play.robot.interfaces.HandlerEvent;
import com.commelina.math24.play.robot.interfaces.MemberEvent;
import com.commelina.math24.play.robot.interfaces.ReadEvent;
import com.commelina.niosocket.proto.*;
import com.commelina.utils.Version;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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

    private final Queue<SocketASK> offlineAskQueue = Queues.newArrayBlockingQueue(11);

    private Lock lock = new ReentrantLock();

    private TokenInterface token;
    private MemberEvent initEvent;

    private int reconnectionTimes;

    public MemberEventLoop(TokenInterface token, MemberEvent initEvent) {
        this.token = token;
        this.initEvent = initEvent;
    }

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

    void connection(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(SocketASK.newBuilder()
                .setForward(SYSTEM_FORWARD_CODE.SYSTEM_VALUE)
                .setBody(RequestBody.newBuilder()
                        .setOpcode(SYSTEM_OPCODE.LOGIN_CODE_VALUE)
                        .setVercode(Version.create("1.0.0"))
                        .addArgs(ByteString.copyFromUtf8(token.getToken()))
                )
                .build());
    }

    void systemResponse(ChannelHandlerContext ctx, SocketMessage message) {
        switch (message.getOpcode()) {
            case SYSTEM_OPCODE.LOGIN_CODE_VALUE:
                switch (message.getBody().getError()) {
                    case SYSTEM_ERROR.LOGIN_SUCCESS_VALUE:
                        this.ctx = ctx;
                        if (reconnectionTimes > 0) {

                        } else {
                            SocketASK ask;
                            do {
                                ask = offlineAskQueue.poll();
                                ctx.writeAndFlush(ask);
                            } while (ask != null);
                        }
                        break;
                    case SYSTEM_ERROR.LOGIN_FAILED_VALUE:
                        // 重置重连次数
                        errorTimes = 0;
                        reconnection();
                        break;
                    default:
                        throw new RuntimeException("undefined error " + message.getBody().getError());
                }
                break;
            default:
                throw new RuntimeException("undefined opcode " + message.getOpcode());
        }
    }

    private void ask(SocketASK message) {
        if (ctx != null) {
            if (offlineAskQueue.size() > 0) {
                // 把消息加入离线消息列表
                offlineAskQueue.add(message);
            } else {
                ctx.writeAndFlush(message);
            }
            return;
        }
        // 把消息加入离线消息列表
        offlineAskQueue.add(message);

        reconnection();
    }

    private void reconnection() {
        if (lock.tryLock()) {
            lock.lock();
            // 记录总共重连的次数
            reconnectionTimes++;
            try {
                // 执行重连操作
                while (errorTimes++ < ERROR_TIME_LIMIT) {
                    if (ctx != null) {
                        LOGGER.info("第{}次重连成功", errorTimes - 1);
                        return;
                    }
                    LOGGER.info("第{}次尝试重连,{}", errorTimes, LocalTime.now().withNano(0));
                    // TODO: 2017/10/13 这里写得怪怪的，先偷懒了
                    try {
                        NettyClient.start(this);
                    } catch (InterruptedException e) {
                        LOGGER.info("第{}次尝试重连错误,{}", errorTimes, e);
                        continue;
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        LOGGER.info("第{}次尝试重连线程错误,{}", errorTimes, e);
                    }
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private int errorTimes = 0;
    private static final int ERROR_TIME_LIMIT = 12;

}
