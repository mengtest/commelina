package com.game.foundation.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/3.
 */
@Component
public class NettyNioSocketServer {

    private static final Logger LOGGER = Logger.getLogger(NettyNioSocketServer.class.getName());

    private Channel serverChannel;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    // 默认线程数是 cpu 核数的两倍
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource(name = "customChannelInitializer")
    private ChannelInitializer channelInitializer;

    private int port;

    public int getPort() {
        if (serverChannel == null) {
            return -1;
        }
        SocketAddress localAddr = serverChannel.localAddress();
        if (!(localAddr instanceof InetSocketAddress)) {
            return -1;
        }
        return ((InetSocketAddress) localAddr).getPort();
    }

    public void bind() throws Exception {
        ServerBootstrap boot = new ServerBootstrap();  //server启动管理配置
        boot.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, Integer.MAX_VALUE)//最大客户端连接数为 0x7fffffff
                .childHandler(this.channelInitializer);

        // Bind and startWithProtoBuff to accept incoming connections.
        ChannelFuture future = boot.bind(this.port);
        try {
            future.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for bind");
        }
        if (!future.isSuccess()) {
            throw new IOException("Failed to bind", future.cause());
        }
        LOGGER.info(String.format("listen port:%d started.", port));
        serverChannel = future.channel();
    }

    public void shutdown() {
        if (serverChannel == null || !serverChannel.isOpen()) {
            // Already closed.
            return;
        }
        serverChannel.close().addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    public void setPort(int port) {
        this.port = port;
    }

}

class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                throw new Exception("idle exception");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}