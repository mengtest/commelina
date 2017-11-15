package com.commelina.niosocket;

import com.commelina.core.MessageBody;
import com.commelina.niosocket.proto.SocketMessage;

/**
 * @author @panyao
 * @date 2017/8/24
 */
interface MessageResponseBuilder {

    /**
     * 构建 server notify 的消息体
     *
     * @param domain
     * @param opcode
     * @param messageBody
     * @return
     */
    SocketMessage createPushMessage(int domain, int opcode, MessageBody messageBody);

    /**
     * 创建 response 的消息体
     *
     * @param domain
     * @param opcode
     * @param messageBody
     * @return
     */
    SocketMessage createResponseMessage(int domain, int opcode, MessageBody messageBody);

}