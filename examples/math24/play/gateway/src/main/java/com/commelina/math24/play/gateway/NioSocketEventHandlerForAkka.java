package com.commelina.math24.play.gateway;

import akka.actor.ActorSystem;
import akka.util.Timeout;
import com.commelina.akka.dispatching.ClusterActorSystemCreator;
import com.commelina.akka.dispatching.ClusterFrontendActorSystem;
import com.commelina.akka.dispatching.proto.ActorResponse;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.math24.common.proto.DOMAIN;
import com.commelina.math24.play.gateway.proto.ERROR_CODE;
import com.commelina.math24.play.gateway.proto.GATEWAY_METHODS;
import com.commelina.niosocket.ReplyUtils;
import com.commelina.niosocket.SocketEventHandler;
import com.commelina.niosocket.proto.SocketASK;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * @author panyao
 * @date 2017/11/15
 */
@Component
public class NioSocketEventHandlerForAkka implements SocketEventHandler {

    private static final Timeout DEFAULT_TIMEOUT = new Timeout(Duration.create(5, TimeUnit.SECONDS));

    private Dispatching dispatching = new Dispatching();

    @Override
    public void onRequest(ChannelHandlerContext ctx, SocketASK ask) {
        final ApiRequest request = ApiRequest.newBuilder()
                .setOpcode(ask.getOpcode())
                .setVersion(ask.getVersion())
                .addAllArgs(ask.getArgsList())
                .build();

        switch (ask.getForward()) {
            case DOMAIN.GATEWAY_VALUE:
                dispatching.requestGateway(ctx, request);
                break;
            case DOMAIN.MATCHING_VALUE:
                dispatching.requestMatch(ctx, request);
                break;
            default:
                ReplyUtils.reply(ctx, ask.getForward(), ask.getOpcode(), ERROR_CODE.DOMAIN_NOT_FOUND_VALUE);
        }

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

    private static class Dispatching {
        final ActorSystem gateway;

        final ClusterFrontendActorSystem match;
        final ClusterFrontendActorSystem room;

        public Dispatching() {
            gateway = ClusterActorSystemCreator.create("gateway", "gateway");
            match = ClusterActorSystemCreator.createAsClusterFrontend("ClusterMatchingSystem", "cluster-requestGateway-match");
            room = ClusterActorSystemCreator.createAsClusterFrontend("ClusterRoomSystem", "cluster-requestGateway-room");
        }

        public void requestGateway(ChannelHandlerContext ctx, ApiRequest request) {
            switch (request.getOpcode()) {
                case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:

                    break;
                default:
            }

//            ReplyUtils.reply(ctx, askForBackend.getForward(), askForBackend.getOpcode(), body)
        }

        public void requestMatch(ChannelHandlerContext ctx, ApiRequest request) {
            ActorResponse response = match.askForBackend(request);
            ReplyUtils.reply(ctx, DOMAIN.MATCHING_VALUE, request.getOpcode(), response.getMessage());
        }

        private void passortValid(ChannelHandlerContext ctx, ApiRequest request) {

//            if (tokenArg == null) {
//                // token 转换错误
////                ReplyUtils.reply();
//                DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR))
//                return;
//            }
//
//            //        String token = tokenArg.getAsString();
//            //        String parseToken = new String(BaseEncoding.base64Url().decode(token));
//            //        List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
//            //        ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
//            //        ContextAdapter.userLogin(context.channel().id(), tokenArg.getAsLong());
//
//            long userId = Long.valueOf(tokenArg.toStringUtf8());
////            logger().info("userId:{}, 登录成功", userId);
        }

    }

}
