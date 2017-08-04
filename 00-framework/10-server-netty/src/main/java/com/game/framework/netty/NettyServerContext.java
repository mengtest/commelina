package com.game.framework.netty;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by @panyao on 2017/8/3.
 */
@Component
public final class NettyServerContext {

    private BlockingQueue<String> messageQueue = new ArrayBlockingQueue(10000);

    /**
     * 用户 user_id 和当前登录的 channel 的映射
     */
    private Map<Long, SocketChannel> channelMap = new ConcurrentHashMap(10000);

    /**
     * 添加引用关系
     *
     * @param clientId
     * @param socketChannel
     */
    public void addChannelShip(long clientId, SocketChannel socketChannel) {
        channelMap.put(clientId, socketChannel);
    }

    public Channel get(Long clientId) {
        return channelMap.get(clientId);
    }

    public void remove(SocketChannel socketChannel) {
        for (Map.Entry entry : channelMap.entrySet()) {
            if (entry.getValue() == socketChannel) {
                channelMap.remove(entry.getKey());
                break;
            }
        }
    }

    public void listener() {
        for (; ; ) {
            try {
                messageQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
