package com.commelina.sangong;

import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by panyao on 2017/12/2.
 */
public interface MemberEvent {

    // 操作码，参数
    // 用户登陆的uid可以在网络层维护，看你自己取舍
    void onRequest(ChannelHandlerContext ctx, long userId, SocketASK ask);

    // 用户离线 等
    void onOffline(ChannelHandlerContext ctx, long userId);

    // 用户上线
    void onOnline(ChannelHandlerContext ctx, long userId);

}