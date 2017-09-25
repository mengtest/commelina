package com.framework.message;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ResponseMessage {

    private final MessageBus message;

    protected ResponseMessage(MessageBus messageBus) {
        this.message = messageBus;
    }

    public static ResponseMessage newMessage(MessageBus messageBus) {
        return new ResponseMessage(messageBus);
    }

    public MessageBus getMessage() {
        return message;
    }


}
