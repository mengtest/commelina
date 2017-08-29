package com.nexus.maven.core.message;

/**
 * Created by @panyao on 2017/8/29.
 * @deprecated
 */
public final class ACKMessage {

    private final long ackId;

    private ACKMessage(long ackId) {
        this.ackId = ackId;
    }

    public static ACKMessage newAck(long ackId) {
        return new ACKMessage(ackId);
    }

    public long getAckId() {
        return ackId;
    }
}
