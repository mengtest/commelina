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

/**
 * Created by @panyao on 2017/9/11.
 */
public class GatewayLogin implements MemberEvent {

    private final String account;
    private final String pwd;
    private long userId = 1;

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
                .addArgs(0, Arg.newBuilder()
                        .setDataType(DATA_TYPE.LONG)
                        .setValue(ByteString.copyFrom(new byte[]{Long.valueOf(userId).byteValue()})))
                .build();
        ctx.writeAndFlush(ask);
    }

    @Override
    public boolean isMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode) {
        return domain.getNumber() == DOMAIN.GATE_WAY_VALUE &&
                apiOpcode.getNumber() == GATEWAY_METHODS.PASSPORT_CONNECT_VALUE;
    }

    @Override
    public EventResult read(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg) {
        // 登录成功，注册匹配事件
        eventLoop.addEvent(new MatchingJoinMatch(userId));
        return EventResult.REMOVE;
    }
}
