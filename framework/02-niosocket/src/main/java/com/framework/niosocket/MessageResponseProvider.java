package com.framework.niosocket;

/**
 * Created by @panyao on 2017/8/24.
 */
class MessageResponseProvider {

    static final MessageResponseBuilder DEFAULT_MESSAGE_PROVIDER = MessageResponseProvider.produce();

    static MessageResponseBuilder produce() {
        return new MessageResponseBuilderWithProtoBuff();
    }

}
