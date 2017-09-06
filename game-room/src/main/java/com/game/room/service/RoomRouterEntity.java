package com.game.room.service;

import com.framework.akka.ApiRequestWithActor;

/**
 * Created by @panyao on 2017/9/6.
 */
public class RoomRouterEntity {
    private long roomId;
    private ApiRequestWithActor apiRequestWithActor;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public ApiRequestWithActor getApiRequestWithActor() {
        return apiRequestWithActor;
    }

    public void setApiRequestWithActor(ApiRequestWithActor apiRequestWithActor) {
        this.apiRequestWithActor = apiRequestWithActor;
    }
}
