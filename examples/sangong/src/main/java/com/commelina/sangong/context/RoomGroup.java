package com.commelina.sangong.context;

import com.commelina.niosocket.proto.SocketASK;
import com.commelina.sangong.MemberEvent;
import com.commelina.sangong.RoomManger;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by panyao on 2017/12/2.
 */
public class RoomGroup implements MemberEvent, RoomManger {

    public static RoomGroup getRoomManger() {
        return Holder.roomGroup;
    }

    private static class Holder {
        private static RoomGroup roomGroup = new RoomGroup();
    }

    @Override
    public void onRequest(ChannelHandlerContext ctx, long userId, SocketASK ask) {
        switch (ask.getForward()) {
            case 0:
                break;

        }

//        CompletableFuture.supplyAsync(() -> {
//
//        });

    }

    @Override
    public void onOffline(ChannelHandlerContext ctx, long userId) {

    }

    @Override
    public void onOnline(ChannelHandlerContext ctx, long userId) {

    }

    @Override
    public Room createRoom(PlayerEntity entity) {
        return null;
    }

    @Override
    public void deleteRoom(long roomId) {

    }

    @Override
    public Room findRoom(long roomId) {
        return null;
    }

}