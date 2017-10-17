package com.framework.niosocket.message;

import com.framework.core.MessageBody;

import java.io.Serializable;

/**
 *
 * @author @panyao
 * @date 2017/8/25
 */
public class ResponseMessage {

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
