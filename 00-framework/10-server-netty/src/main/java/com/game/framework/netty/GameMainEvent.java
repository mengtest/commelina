package com.game.framework.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
public class GameMainEvent {

    private static final Logger log = Logger.getLogger(GameMainEvent.class.getName());

    private Channel channel;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    private GameServerHandler gameServerHandler;

    @Resource
    private NettyServerContext context;

    @Resource
    private SessionCache sessionCache;

    public int getPort() {
        if (channel == null) {
            return -1;
        }
        SocketAddress localAddr = channel.localAddress();
        if (!(localAddr instanceof InetSocketAddress)) {
            return -1;
        }
        return ((InetSocketAddress) localAddr).getPort();
    }

    public void bind(int port, final SessionInterface sessionInterface) throws Exception {
        ServerBootstrap b = new ServerBootstrap();//server启动管理配置
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)//最大客户端连接数为1024
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        //这里我们先不写代码，这里主要是添加业务处理handler
                        ch.pipeline().addLast(gameServerHandler);
                    }
                });

        // Bind and start to accept incoming connections.
        ChannelFuture future = b.bind(port);
        try {
            future.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for bind");
        }
        if (!future.isSuccess()) {
            throw new IOException("Failed to bind", future.cause());
        }
        log.info(String.format("port:%d started.", port));
        channel = future.channel();
        // 发送消息的 listener
        log.info(String.format("message queue is listener."));
        context.listener();
    }

    public void shutdown() {
        if (channel == null || !channel.isOpen()) {
            // Already closed.
            return;
        }
        channel.close().addListener(ChannelFutureListener.CLOSE);
//        channel.close().addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
//        channel.close().addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

}
