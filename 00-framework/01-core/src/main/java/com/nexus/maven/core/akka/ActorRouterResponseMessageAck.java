package com.nexus.maven.core.akka;

import com.google.protobuf.Internal;
import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.core.message.ResponseMessage;

/**
 * Created by @panyao on 2017/8/29.
 * @deprecated
 */
public class ActorRouterResponseMessageAck extends ResponseMessage{

    protected ActorRouterResponseMessageAck(Internal.EnumLite opcode, MessageBus messageBus) {
        super(opcode, messageBus);
    }
}
