package com.commelina.sangong;

/**
 * Created by panyao on 2017/12/2.
 */
public interface MemberEvent {

    // 操作码，参数
    // 用户登陆的uid可以在网络层维护，看你自己取舍
    default void onRequest(ChannelHandlerContext context, int userId) {

    }

    // 用户离线 等
    default void onOffline(ChannelHandlerContext context){

    }

}
