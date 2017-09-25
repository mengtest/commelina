package com.game.gateway.router_v3;

import com.framework.message.ApiRequest;
import com.framework.message.BusinessMessage;
import com.framework.message.RequestArg;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ContextAdapter;
import com.framework.niosocket.NioSocketRouter;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.RequestHandler;
import com.game.gateway.MessageProvider;
import com.game.gateway.proto.DOMAIN;
import com.game.gateway.proto.ERROR_CODE;
import com.game.gateway.proto.GATEWAY_APIS;
import com.game.gateway.proto.GATEWAY_METHODS;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by @panyao on 2017/9/22.
 */
@NioSocketRouter(apiPathCode = GATEWAY_APIS.GATEWAY_V1_0_0_VALUE)
public class Gateway implements RequestHandler {

    @Override
    public void onRequest(ApiRequest request, ChannelHandlerContext context) {
        switch (request.getApiOpcode().getNumber()) {
            case GATEWAY_METHODS.PASPPORT_CONNECT_VALUE:
                RequestArg tokenArg = request.getArg(0);
                if (tokenArg == null) {
                    // token 转换错误
                    ReplyUtils.reply(context, DOMAIN.GATE_WAY, ResponseMessage.newMessage(request.getApiOpcode(),
                            MessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR))));
                    return;
                }
//                    String token = tokenArg.getAsString();
//                    String parseToken = new String(BaseEncoding.base64Url().decode(token));
//                    List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
//                    ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
                ContextAdapter.userLogin(context.channel().id(), tokenArg.getAsLong());

                // FIXME: 2017/8/30 登陆成功，返回用户状态，如果是 in game 就走重连机制
                // 回复自己完成了操作
                ReplyUtils.reply(context, DOMAIN.GATE_WAY, ResponseMessage.newMessage(request.getApiOpcode(), MessageProvider.produceMessage()));
        }
    }

}
