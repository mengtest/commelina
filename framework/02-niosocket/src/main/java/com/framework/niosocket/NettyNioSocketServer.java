package com.framework.niosocket;

import com.framework.niosocket.proto.SocketASK;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
/**
 * Created by @panyao on 2017/8/3.
 */
public class NettyNioSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyNioSocketServer.class);
    private Channel serverChannel;
    private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    // 默认线程数是 cpu 核数的两倍
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

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

    public void bind(String host, int port, final RouterContext router) throws IOException {
        ServerBootstrap boot = new ServerBootstrap();  //server启动管理配置
        boot.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, Integer.MAX_VALUE)//最大客户端连接数为 0x7fffffff
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 心跳检查 5s 检查一次，意思就是 10s 服务端就会断开连接
                        ch.pipeline().addLast("heartbeatHandler", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                        // 闲置事件
                        ch.pipeline().addLast("heartbeatTrigger", new ChannelInboundHandlerAcceptorIdleStateTrigger());

//                        字符串协议
//                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

                        // protocol 协议
                        ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                        ch.pipeline().addLast(new ProtobufDecoder(SocketASK.getDefaultInstance()));
                        ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                        ch.pipeline().addLast(new ProtobufEncoder());
                        ChannelInboundHandlerRouterContextAdapter routerAdapter = new ChannelInboundHandlerRouterContextAdapter();
                        routerAdapter.setRouterContext(router);
                        ch.pipeline().addLast(routerAdapter);
                    }
                });

        // Bind and start to accept incoming connections.
        ChannelFuture future = boot.bind(host, port);
        try {
            future.await();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted waiting for bind");
        }
        if (!future.isSuccess()) {
            throw new IOException("Failed to bind", future.cause());
        }
        LOGGER.info("listen port:{} started.", port);
        serverChannel = future.channel();
    }

    public void shutdown() {
        if (serverChannel == null || !serverChannel.isOpen()) {
            // Already closed.
            return;
        }
        serverChannel.close().addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

}

