package com.commelina.math24.play.gateway;

import akka.actor.ActorSystem;
import com.commelina.akka.dispatching.ActorSystemCreator;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.core.MessageBody;
import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.niosocket.ReplyUtils;
import com.commelina.niosocket.SocketEventHandler;
import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author panyao
 * @date 2017/11/15
 */
@Component
public class NioSocketEventHandlerForAkka implements SocketEventHandler {

    private ActorSystem gateway;
    private ActorSystem match;
    private ActorSystem room;

    @Override
    public void onRequest(ChannelHandlerContext ctx, SocketASK ask) {

        ApiRequest request = ApiRequest.newBuilder().setOpcode(ask.getOpcode())
                .setVersion(ask.getVersion())
                .addAllArgs(ask.getArgsList())
                .build();

        MessageBody body;

        switch (ask.getForward()) {
            case DOMAIN.GATEWAY_VALUE:
//                body = gateway.askActor(request);
                body = null;
                break;

            default:
                body = null;
        }

        ReplyUtils.reply(ctx, ask.getForward(), ask.getOpcode(), body);
    }

    @Override
    public void onOnline(ChannelHandlerContext ctx) {

    }

    @Override
    public void onOffline(ChannelHandlerContext ctx, long logoutUserId) {

    }

    @Override
    public void onException(ChannelHandlerContext ctx, Throwable cause) {

    }

    @PostConstruct
    public void createAkkaSystem() {
        gateway = ActorSystemCreator.create("gateway", "gateway");

        match = ActorSystemCreator.createAsCluster("ClusterMatchingSystem", "cluster-gateway-match");
        room = ActorSystemCreator.createAsCluster("ClusterRoomSystem", "cluster-gateway-room");
    }

}
