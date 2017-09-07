package com.game.room.service;

import com.framework.message.ApiLoginRequest;

/**
 * Created by @panyao on 2017/9/6.
 */
public class RoomClientRouterEntity {
    private long roomId;
    private ApiLoginRequest apiLoginRequest;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public ApiLoginRequest getApiLoginRequest() {
        return apiLoginRequest;
    }

    public void setApiLoginRequest(ApiLoginRequest apiLoginRequest) {
        this.apiLoginRequest = apiLoginRequest;
    }
}
