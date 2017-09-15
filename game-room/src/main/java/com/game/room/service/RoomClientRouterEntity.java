package com.game.room.service;

import com.framework.message.ApiRequestLogin;

/**
 * Created by @panyao on 2017/9/6.
 */
public class RoomClientRouterEntity {
    private long roomId;
    private ApiRequestLogin apiRequestLogin;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public ApiRequestLogin getApiRequestLogin() {
        return apiRequestLogin;
    }

    public void setApiRequestLogin(ApiRequestLogin apiRequestLogin) {
        this.apiRequestLogin = apiRequestLogin;
    }
}
