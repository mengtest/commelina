package com.game.room.event;

import com.game.room.entity.PlayerStatus;

/**
 * Created by @panyao on 2017/10/11.
 */
public class PlayerStatusEvent {
    private final long userId;
    private final PlayerStatus status;

    public PlayerStatusEvent(long userId, PlayerStatus status) {
        this.userId = userId;
        this.status = status;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public long getUserId() {
        return userId;
    }

}
