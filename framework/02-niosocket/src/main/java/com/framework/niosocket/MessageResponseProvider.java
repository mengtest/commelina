package com.framework.niosocket;

/**
 * Created by @panyao on 2017/8/24.
 */
public class MessageResponseProvider {

   public static final MessageResponseBuilder DEFAULT_MESSAGE_RESPONSE = MessageResponseProvider.produce();

    static MessageResponseBuilder produce() {
        return new MessageResponseBuilderWithProtoBuff();
    }

}
