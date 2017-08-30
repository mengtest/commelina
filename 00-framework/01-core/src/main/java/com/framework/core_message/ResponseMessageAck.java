package com.framework.core_message;

/**
 * Created by @panyao on 2017/8/25.
 * @deprecated
 */
public class ResponseMessageAck {

    private final long ackId;
    private final ResponseMessage message;

    protected ResponseMessageAck(long ackId, ResponseMessage message) {
        this.ackId = ackId;
        this.message = message;
    }

    public static ResponseMessageAck newNotifyAck(long ackId, ResponseMessage message) {
        return new ResponseMessageAck(ackId, message);
    }

    public long getAckId() {
        return ackId;
    }

    public ResponseMessage getMessage() {
        return message;
    }

}
