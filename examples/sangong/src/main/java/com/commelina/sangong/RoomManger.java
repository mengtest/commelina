package com.commelina.sangong;

import com.commelina.sangong.context.PlayerEntity;
import com.commelina.sangong.context.Room;

/**
 * @author panyao
 * @date 2017/12/6
 */
public interface RoomManger {

    /**
     * @param entity
     * @return
     */
    Room createRoom(PlayerEntity entity);

    void deleteRoom(long roomId);

    Room findRoom(long roomId);

}
