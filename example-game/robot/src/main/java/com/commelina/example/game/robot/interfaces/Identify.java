package com.commelina.example.game.robot.interfaces;

import com.google.protobuf.Internal;

/**
 *
 * @author @panyao
 * @date 2017/9/11
 */
public interface Identify {

    Internal.EnumLite getDomain();

    Internal.EnumLite getApiOpcode();

}
