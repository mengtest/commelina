package com.game.robot.events;

import com.framework.niosocket.proto.SocketASK;
import com.framework.niosocket.proto.SocketMessage;
import com.game.common.proto.DOMAIN;
import com.game.gateway.proto.GATEWAY_APIS;
import com.game.gateway.proto.GATEWAY_METHODS;
import com.game.robot.interfaces.MemberEvent;
import com.game.robot.interfaces.MemberEventLoop;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by @panyao on 2017/9/11.
 */
public class GatewayLogin implements MemberEvent {

    private Logger LOGGER = LoggerFactory.getLogger(GatewayLogin.class);

    private final String account;
    private final String pwd;

    public GatewayLogin(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    @Override
    public SocketASK handler(MemberEventLoop eventLoop) {
        // FIXME: 2017/9/11 获取 token

        LOGGER.debug("向服务器发送登录请求。");

        return SocketASK.newBuilder()
                .setForward(GATEWAY_APIS.GATEWAY_V1_0_0_VALUE)
                .setOpcode(GATEWAY_METHODS.PASSPORT_CONNECT_VALUE)
                .setVersion("1.0.0")
                .addArgs(ByteString.copyFromUtf8("1"))
                .build();
    }

    @Override
    public Internal.EnumLite getDomain() {
        return DOMAIN.GATE_WAY;
    }

    @Override
    public Internal.EnumLite getApiOpcode() {
        return GATEWAY_METHODS.PASSPORT_CONNECT;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, SocketMessage msg) {
        eventLoop.addEvent(new MatchingJoinMatch(1L));

        LOGGER.info("登录成功，注册匹配事件。");

        return EventResult.REMOVE;
    }
}
