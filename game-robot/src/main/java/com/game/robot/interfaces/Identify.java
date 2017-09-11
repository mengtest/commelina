package com.game.robot.interfaces;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/11.
 */
public interface Identify {

    boolean isMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode);

}
