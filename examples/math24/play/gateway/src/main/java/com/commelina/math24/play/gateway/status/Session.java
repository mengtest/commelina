package com.commelina.math24.play.gateway.status;

import com.commelina.akka.dispatching.nodes.AbstractServiceActor;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.google.protobuf.ByteString;

/**
 * @author panyao
 * @date 2017/11/16
 */
public class Session extends AbstractServiceActor {

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, r -> {
                    ByteString tokenArg = r.getArgs(0);
                    if (tokenArg == null) {
                        // token 转换错误
//                        response(DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR)));
                        return;
                    }

                    //        String token = tokenArg.getAsString();
                    //        String parseToken = new String(BaseEncoding.base64Url().decode(token));
                    //        List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
                    //        ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
                    //        ContextAdapter.userLogin(context.channel().id(), tokenArg.getAsLong());

                    long userId = Long.valueOf(tokenArg.toStringUtf8());
                    getLogger().info("userId:{}, 登录成功", userId);
//                    getSender().tell(new LoginUserEntity(userId, DefaultMessageProvider.produceEmptyMessage()), getSelf());
                })
                .build();
    }

}
