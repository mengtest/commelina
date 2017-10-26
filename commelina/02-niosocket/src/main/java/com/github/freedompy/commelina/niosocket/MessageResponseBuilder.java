package com.github.freedompy.commelina.niosocket;

import com.github.freedompy.commelina.core.MessageBody;
import com.github.freedompy.commelina.niosocket.proto.SocketMessage;
import com.google.protobuf.Internal;

/**
 * @author @panyao
 * @date 2017/8/24
 */
public interface MessageResponseBuilder {

    /**
     * 构建 server notify 的消息体
     *
     * @param domain
     * @param opcode
     * @param messageBody
     * @return
     */
    SocketMessage createPushMessage(Internal.EnumLite domain, int opcode, MessageBody messageBody);

    /**
     * 创建 response 的消息体
     *
     * @param domain
     * @param opcode
     * @param messageBody
     * @return
     */
    SocketMessage createResponseMessage(Internal.EnumLite domain, int opcode, MessageBody messageBody);

}