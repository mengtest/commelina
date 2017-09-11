package com.game.robot.events;

import com.framework.niosocket.proto.Arg;
import com.framework.niosocket.proto.DATA_TYPE;
import com.framework.niosocket.proto.SocketASK;
import com.framework.niosocket.proto.SocketMessage;
import com.game.gateway.proto.DOMAIN;
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

    public GatewayLogin(String account, String pwd) {
        this.account = account;
        this.pwd = pwd;
    }

    @Override
    public void member(MemberEventLoop eventLoop, ChannelHandlerContext ctx) {
        // FIXME: 2017/9/11 获取 token
        SocketASK ask = SocketASK.newBuilder()
                .setApiCode(GATEWAY_APIS.GATEWAY_V1_0_0_VALUE)
                .setApiMethod(GATEWAY_METHODS.PASPPORT_CONNECT_VALUE)
                .setVersion("1.0.0")
                .setArgs(0, Arg.newBuilder()
                        .setDataType(DATA_TYPE.LONG)
                        .setValue(ByteString.copyFrom(new byte[]{Long.valueOf(1).byteValue()}))
                )
                .build();
        ctx.writeAndFlush(ask);
    }

    @Override
    public boolean isReadMe(Internal.EnumLite domain, Internal.EnumLite apiOpcode) {
        return domain.getNumber() == DOMAIN.GATE_WAY_VALUE &&
                apiOpcode.getNumber() == GATEWAY_METHODS.PASPPORT_CONNECT_VALUE;
    }

    @Override
    public EventResult channelRead(MemberEventLoop eventLoop, ChannelHandlerContext context, SocketMessage msg) {
        // 用户登录 app 就自己去匹配
        eventLoop.executeMemberEvent(new MatchingJoinMatch(1L));
        return EventResult.REMOVE;
    }
}
