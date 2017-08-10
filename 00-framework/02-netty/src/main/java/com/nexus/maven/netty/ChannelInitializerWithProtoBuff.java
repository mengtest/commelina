package com.nexus.maven.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.socket.netty.proto.SocketNettyProtocol;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by @panyao on 2017/8/9.
 */
@Component("customChannelInitializer")
public class ChannelInitializerWithProtoBuff extends ChannelInitializer<SocketChannel> {

    @Resource
    private SocketServerHandler serverHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 心跳检查 5s 检查一次，意思就是 10s 服务端就会断开连接
        pipeline.addLast("heartbeatHandler", new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
        // 闲置事件
        pipeline.addLast("heartbeatTrigger", new AcceptorIdleStateTrigger());

//                        字符串协议
//                        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                        pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
//                        pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                        pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

        // protocol 协议
        pipeline.addLast(new ProtobufVarint32FrameDecoder());
        pipeline.addLast(new ProtobufDecoder(SocketNettyProtocol.SocketASK.getDefaultInstance()));
        pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
        pipeline.addLast(new ProtobufEncoder());

        pipeline.addLast(serverHandler);
    }

}
