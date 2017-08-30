package com.framework.coreakka;

import com.google.protobuf.Internal;
import com.framework.core_message.MessageBus;
import com.framework.core_message.ResponseMessage;

/**
 * Created by @panyao on 2017/8/29.
 * @deprecated
 */
public class ActorRouterResponseMessageAck extends ResponseMessage{

    protected ActorRouterResponseMessageAck(Internal.EnumLite opcode, MessageBus messageBus) {
        super(opcode, messageBus);
    }
}
