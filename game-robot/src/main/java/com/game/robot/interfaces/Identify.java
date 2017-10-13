package com.game.robot.interfaces;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/11.
 */
public interface Identify {

    Internal.EnumLite getDomain();

    Internal.EnumLite getApiOpcode();

}
