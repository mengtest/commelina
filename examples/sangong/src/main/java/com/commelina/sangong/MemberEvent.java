package com.commelina.sangong;

import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by panyao on 2017/12/2.
 */
public interface MemberEvent {

    /**
     *
     * @param ctx
     * @param userId
     * @param ask
     */
    void onRequest(ChannelHandlerContext ctx, long userId, SocketASK ask);

    /**
     * 用户离线 等
     * @param ctx
     * @param userId
     */
    void onOffline(ChannelHandlerContext ctx, long userId);

    /**
     * 用户上线
     * @param ctx
     * @param userId
     */
    void onOnline(ChannelHandlerContext ctx, long userId);

}