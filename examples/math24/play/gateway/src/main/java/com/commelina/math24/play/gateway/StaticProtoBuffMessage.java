package com.commelina.math24.play.gateway;

import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.gateway.proto.ERROR_CODE;
import com.commelina.niosocket.ProtoBuffStatic;
import com.commelina.niosocket.proto.SERVER_CODE;
import com.commelina.niosocket.proto.SocketMessage;

/**
 * @author panyao
 * @date 2017/11/16
 */
public interface StaticProtoBuffMessage {

    /**
     * 域没有找到
     */
    SocketMessage DOMAIN_NOT_FOUND = ProtoBuffStatic.createMessage(SERVER_CODE.RESONSE_CODE, DOMAIN.GATEWAY_VALUE, ERROR_CODE.DOMAIN_NOT_FOUND_VALUE);

}
