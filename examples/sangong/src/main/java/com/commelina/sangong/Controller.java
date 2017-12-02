package com.commelina.sangong;

import com.commelina.sangong.context.Room;

/**
 * Created by panyao on 2017/12/2.
 */
public interface Controller{

    // 返回延迟操作的时间 格式自定定
    default int onStart(Room room) {
        // 给客户端广播进入下一阶段的消息
        return 0;
    }

    // 执行第一帧
    default int onUpdate(Room room) {

        return 0;
    }

    // 是否结束
    default boolean isOver(Room room){

    }

    // 结束之后触发的操作，比如进入下一阶段
    default void onCompleted(Room room) {


    }

}