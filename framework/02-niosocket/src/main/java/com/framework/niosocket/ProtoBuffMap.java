package com.framework.niosocket;

import com.framework.niosocket.proto.SERVER_CODE;
import com.framework.niosocket.proto.SocketMessage;

/**
 * Created by @panyao on 2017/9/27.
 */
public class ProtoBuffMap {

    public static final SocketMessage HEARTBEAT_CODE = createErrorMessage(SERVER_CODE.HEARTBEAT_CODE);
    public static final SocketMessage RPC_API_NOT_FOUND = createErrorMessage(SERVER_CODE.RPC_API_NOT_FOUND);
    public static final SocketMessage SERVER_ERROR = createErrorMessage(SERVER_CODE.SERVER_ERROR);

    static SocketMessage createErrorMessage(SERVER_CODE serverCode) {
        return SocketMessage
                .newBuilder()
                .setCode(serverCode)
                .build();
    }

}
