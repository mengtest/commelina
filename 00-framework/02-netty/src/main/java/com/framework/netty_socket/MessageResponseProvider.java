package com.framework.netty_socket;

/**
 * Created by @panyao on 2017/8/24.
 */
class MessageResponseProvider {

    static final MessageResponseBuilder DEFAULT_MESSAGE_RESPONSE = MessageResponseProvider.produce();

    static MessageResponseBuilder produce() {
        return new MessageResponseBuilderWithProtoBuff();
    }

}
