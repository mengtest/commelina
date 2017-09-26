package com.game.room.service;

import com.framework.message.ApiRequest;

/**
 * Created by @panyao on 2017/9/6.
 */
public class RoomClientRouterEntity {
    private long roomId;
    private ApiRequest apiRequest;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public ApiRequest getApiRequest() {
        return apiRequest;
    }

    public void setApiRequest(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }
}
