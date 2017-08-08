package com.game.foundation.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private NettyServerContext nettyServerContext;

    @Resource
    private MessageAdapter messageAdapter;

    private int port;

    private RPCRouterDispatchInterface dispatch;

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
        ServerBootstrap b = new ServerBootstrap();//server启动管理配置
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, Integer.MAX_VALUE)//最大客户端连接数为 0x7fffffff
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();

                        // 心跳检查 5s 检查一次，意思就是 10s 服务端就会断开连接
                        pipeline.addLast("heartbeatHandler", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                        // 闲置事件
                        pipeline.addLast("heartbeatTrigger", new AcceptorIdleStateTrigger());

                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));

                        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

                        pipeline.addLast(new ChannelInboundHandlerAdapter() {

                            //当客户端连上服务器的时候会触发此函数
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                boolean result = nettyServerContext.channelActive(ctx.channel());
                                LOGGER.info("client:" + ctx.channel().id() + ", login server:" + result);
                            }

                            //当客户端断开连接的时候触发函数
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                long logoutUserId = nettyServerContext.channelInactivee(ctx.channel());
                                LOGGER.info("client:" + ctx.channel().id() + ", logout userId:" + logoutUserId);
                            }

                            //当客户端发送数据到服务器会触发此函数
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 当前 channel 未登录
                                NettyNioSocketServer.this.dispatch.invoke(nettyServerContext.channelLoginUserId(ctx.channel().id()), ctx, msg);
                            }

                            // 调用异常的处理
                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                cause.printStackTrace();
                                ctx.close();
                            }

                        });
                    }
                });

        // Bind and start to accept incoming connections.
        ChannelFuture future = b.bind(this.port);
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

    public RPCRouterDispatchInterface getDispatch() {
        return dispatch;
    }

    public void setDispatch(RPCRouterDispatchInterface dispatch) {
        this.dispatch = dispatch;
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