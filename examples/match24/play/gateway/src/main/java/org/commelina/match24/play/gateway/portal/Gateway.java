package org.commelina.match24.play.gateway.portal;

import org.commelina.akka.dispatching.DefaultLocalActorRequestHandler;
import org.commelina.akka.dispatching.proto.ApiRequest;
import org.commelina.core.BusinessMessage;
import org.commelina.core.DefaultMessageProvider;
import org.commelina.core.MessageBody;
import org.commelina.example.game.common.proto.DOMAIN;
import org.commelina.match24.play.gateway.proto.ERROR_CODE;
import org.commelina.match24.play.gateway.proto.GATEWAY_METHODS;
import org.commelina.niosocket.ContextAdapter;
import org.commelina.niosocket.NioSocketRouter;
import org.commelina.niosocket.ReplyUtils;
import org.commelina.niosocket.proto.SocketASK;
import com.google.protobuf.Internal;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author @panyao
 * @date 2017/9/22
 */
@NioSocketRouter(forward = DOMAIN.GATEWAY_VALUE)
public class Gateway extends DefaultLocalActorRequestHandler {

    private final MessageBody messageBody = DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.GATEWAY_API_UNAUTHORIZED));

    @Override
    protected boolean beforeHook(SocketASK ask, ApiRequest.Builder newRequestBuilder, ChannelHandlerContext ctx) {

        switch (ask.getOpcode()) {
            // 登录接口允许匿名
            case GATEWAY_METHODS.PASSPORT_CONNECT_VALUE:
                return true;
            default:

        }

        final long userId = ContextAdapter.getLoginUserId(ctx.channel().id());
        if (userId <= 0) {
            ReplyUtils.reply(ctx, DOMAIN.GATEWAY, ask.getOpcode(), messageBody);
            return false;
        }

        newRequestBuilder.setLoginUserId(userId);

        return true;
    }

    @Override
    public final Internal.EnumLite getRouterId() {
        return DOMAIN.GATEWAY;
    }

}