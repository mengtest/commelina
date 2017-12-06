package com.commelina.sangong;

import com.commelina.niosocket.SocketEventHandler;
import com.commelina.niosocket.proto.SocketASK;
import com.commelina.sangong.context.RoomGroup;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author panyao
 * @date 2017/11/15
 */
@Component
public class NioSocketEventHandler implements SocketEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(NioSocketEventHandler.class);

    @Override
    public void onRequest(ChannelHandlerContext ctx, long userId, SocketASK ask) {
        RoomGroup.getRoomManger().onRequest(ctx, userId, ask);
    }

    @Override
    public CompletableFuture<Long> onLogin(ChannelHandlerContext ctx, SocketASK ask) {
        // 整个消息就是 token
        ByteString tokenArg = ask.getBody().getArgs(0);
        if (tokenArg == null) {
            logger.info("Token arg must be input.");
            return null;
        }
        String token = tokenArg.toStringUtf8();
        if (Strings.isNullOrEmpty(token)) {
            logger.info("Token arg must be input.");
            return null;
        }
        return CompletableFuture.supplyAsync(() -> {
            String parseToken = new String(BaseEncoding.base64Url().decode(token));
            List<String> tokenChars = Splitter.on('|').splitToList(parseToken);

            Long userId = Long.valueOf(tokenArg.toStringUtf8());
            RoomGroup.getRoomManger().onOnline(ctx, userId);
            return userId;
        });
    }

    @Override
    public void onOffline(ChannelHandlerContext ctx, long logoutUserId) {
        RoomGroup.getRoomManger().onOffline(ctx, logoutUserId);
    }

    @Override
    public void onException(ChannelHandlerContext ctx, Throwable cause) {
        if (logger.isDebugEnabled()) {
            cause.printStackTrace();
        } else {
            logger.error("{}", cause);
        }
    }

}