package com.commelina.math24.play.room.message;

/**
 * @author panyao
 * @date 2017/10/24
 */
public class NotifyJoinRoom {
    /**
     * 房间id
     */
    private long roomId;
    /**
     * 结束时间
     */
    private long overMicrosecond;

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getOverMicrosecond() {
        return overMicrosecond;
    }

    public void setOverMicrosecond(long overMicrosecond) {
        this.overMicrosecond = overMicrosecond;
    }

}
