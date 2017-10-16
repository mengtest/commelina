package com.framework.niosocket.message;

import com.framework.core.MessageBody;

import java.io.Serializable;

/**
 * Created by @panyao on 2017/8/25.
 */
public class ResponseMessage implements Serializable {

    private final MessageBody message;

    protected ResponseMessage(MessageBody messageBody) {
        this.message = messageBody;
    }

    public static ResponseMessage newMessage(MessageBody messageBody) {
        return new ResponseMessage(messageBody);
    }

    public MessageBody getMessage() {
        return message;
    }


}
