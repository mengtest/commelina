package com.github.freedompy.commelina.niosocket;

/**
 * 默认的消息生成器
 *
 * @author @panyao
 * @date 2017/8/24
 */
class MessageResponseProvider {

    static final MessageResponseBuilder DEFAULT_MESSAGE_PROVIDER = MessageResponseProvider.produce();

    static MessageResponseBuilder produce() {
        return new MessageResponseBuilderWithProtoBuff();
    }

}
