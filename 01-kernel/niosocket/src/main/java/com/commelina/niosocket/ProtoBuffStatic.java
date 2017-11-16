package com.commelina.niosocket;

import com.commelina.niosocket.proto.SERVER_CODE;
import com.commelina.niosocket.proto.SocketMessage;

/**
 * 静态的 proto 映射
 *
 * @author @panyao
 * @date 2017/9/27
 */
public class ProtoBuffStatic {

    public static final SocketMessage HEARTBEAT_CODE = SocketMessage.getDefaultInstance();
    public static final SocketMessage SERVER_ERROR = SocketMessage
            .newBuilder()
            .setCode(SERVER_CODE.SERVER_ERROR)
            .build();

    public static SocketMessage createMessage(SERVER_CODE serverCode, int domain, int opcode) {
        return SocketMessage
                .newBuilder()
                .setCode(serverCode)
                .setDomain(domain)
                .setOpcode(opcode)
                .build();
    }

}