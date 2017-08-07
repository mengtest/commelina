package com.game.framework.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
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
public class GameServer {

    private static final Logger log = Logger.getLogger(GameServer.class.getName());

    private Channel channel;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    private NettyServerContext nettyServerContext;

    @Resource
    private MessageAdapter messageAdapter;

    @Resource
    private SessionCache sessionCache;

    private int port;

    private SessionInterface sessionInterface;

    private RPCRouterDispatchInterface dispatch;

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

    public void bind() throws Exception {
        sessionCache.setSessionInterface(this.sessionInterface);
        ServerBootstrap b = new ServerBootstrap();//server启动管理配置

        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, Integer.MAX_VALUE)//最大客户端连接数为 0x7fffffff
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));

                        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                        //这里我们先不写代码，这里主要是添加业务处理handler
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            //当客户端连上服务器的时候会触发此函数
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                boolean result = NettyServerContext.CHANNEL_GROUP.add(ctx.channel());
                                log.info("client:" + ctx.channel().id() + ", login server:" + result);
                                nettyServerContext.userJoin(1, ctx.channel().id());
                            }

                            //当客户端断开连接的时候触发函数
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                boolean result = NettyServerContext.CHANNEL_GROUP.remove(ctx.channel());
                                log.info("client:" + ctx.channel().id() + ", logout server:" + result);
                                nettyServerContext.userRemove(1);
                            }

                            //当客户端发送数据到服务器会触发此函数
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                GameServer.this.dispatch.invoke(ctx, msg);
//                                ctx.writeAndFlush("response");
                            }

                            // 调用异常的处理
                            @Override
                            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                //cause.printStackTrace();
                                log.info(cause.getMessage());
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
        log.info(String.format("port:%d started.", port));
        channel = future.channel();
        // 发送消息的 listener
        log.info(String.format("message queue listener started."));
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

    public void setPort(int port) {
        this.port = port;
    }

    public SessionInterface getSessionInterface() {
        return sessionInterface;
    }

    public void setSessionInterface(SessionInterface sessionInterface) {
        this.sessionInterface = sessionInterface;
    }

    public RPCRouterDispatchInterface getDispatch() {
        return dispatch;
    }

    public void setDispatch(RPCRouterDispatchInterface dispatch) {
        this.dispatch = dispatch;
    }
}
