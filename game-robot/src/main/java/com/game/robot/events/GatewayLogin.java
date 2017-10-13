package com.game.robot.events;

import com.framework.niosocket.proto.Arg;
import com.framework.niosocket.proto.DATA_TYPE;
import com.framework.niosocket.proto.SocketASK;
import com.framework.niosocket.proto.SocketMessage;
import com.game.common.proto.DOMAIN;
import com.game.gateway.proto.GATEWAY_APIS;
import com.game.gateway.proto.GATEWAY_METHODS;
import com.game.robot.interfaces.MemberEvent;
import com.game.robot.interfaces.MemberEventLoop;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;
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
    public void handle(MemberEventLoop eventLoop, ChannelHandlerContext ctx) {
        // FIXME: 2017/9/11 获取 token

        SocketASK ask = SocketASK.newBuilder()
                .setForward(GATEWAY_APIS.GATEWAY_V1_0_0_VALUE)
                .setOpcode(GATEWAY_METHODS.PASSPORT_CONNECT_VALUE)
                .setVersion("1.0.0")
                .addArgs(Arg.newBuilder()
                        .setDataType(DATA_TYPE.LONG)
                        .setValue(ByteString.copyFromUtf8("1")))
                .build();
        LOGGER.debug("向服务器发送登录请求。");
        ctx.writeAndFlush(ask);
    }

    @Override
    public boolean isMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode) {
        return domain.getNumber() == DOMAIN.GATE_WAY_VALUE &&
                apiOpcode.getNumber() == GATEWAY_METHODS.PASSPORT_CONNECT_VALUE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg) {
        eventLoop.addEvent(new MatchingJoinMatch(1L));
        LOGGER.info("登录成功，注册匹配事件。");
        return EventResult.REMOVE;
    }
}
