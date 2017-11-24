package com.commelina.math24.play.robot.interfaces;

import com.google.protobuf.Internal;

/**
 * 事件标志接口
 *
 * @author @panyao
 * @date 2017/9/11
 */
public interface Identify {

    /**
     * 判断是否是对应 onCreatedAsk
     *
     * @param forward
     * @param opcode
     * @return
     */
    boolean match(Internal.EnumLite forward, Internal.EnumLite opcode);

}
