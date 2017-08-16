package com.game.foundation.gateway.api;

import com.game.foundation.gateway.MessageProvider;
import com.game.foundation.gateway.OpCodeConstants;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import com.nexus.maven.netty.socket.NettyServerContext;
import com.nexus.maven.netty.socket.router.DefaultResponseHandler;
import com.nexus.maven.netty.socket.router.ResponseHandler;
import com.nexus.maven.netty.socket.router.RpcApi;
import com.nexus.maven.netty.socket.router.RpcMethod;
import io.netty.channel.ChannelHandlerContext;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by @panyao on 2017/8/8.
 */
@RpcApi
public class Passport {

    @Resource
    private NettyServerContext context;

    @RpcMethod(value = "connect")
    public ResponseHandler connect(ChannelHandlerContext ctx, String token) {
        // FIXME: 2017/8/11 测试的时候不用验证
        String parseToken = new String(BaseEncoding.base64Url().decode(token));
        List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
        context.userJoin(ctx.channel().id(), Long.valueOf(tokenChars.get(0)));
        return DefaultResponseHandler.newHandler(MessageProvider.newMessage(OpCodeConstants.PASSPORT_CONNECT));
    }

}