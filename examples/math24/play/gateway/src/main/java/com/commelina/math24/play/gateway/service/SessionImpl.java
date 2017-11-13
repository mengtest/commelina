package com.commelina.math24.play.gateway.service;

import com.commelina.akka.dispatching.ActorServiceHandler;
import com.commelina.akka.dispatching.LocalServiceHandler;
import com.commelina.akka.dispatching.LoginUserEntity;
import com.commelina.akka.dispatching.local.AbstractLocalServiceActor;
import com.commelina.akka.dispatching.local.AkkaLocalWorkerSystem;
import com.commelina.akka.dispatching.proto.ApiRequest;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;
import com.commelina.core.BusinessMessage;
import com.commelina.core.DefaultMessageProvider;
import com.commelina.math24.play.gateway.proto.ERROR_CODE;
import com.commelina.math24.play.gateway.proto.GATEWAY_METHODS;
import com.google.protobuf.ByteString;
import com.google.protobuf.Internal;

/**
 * @author @panyao
 * @date 2017/9/25
 */
@ActorServiceHandler
public class SessionImpl implements LocalServiceHandler {

    @Override
    public Internal.EnumLite getRouterId() {
        return GATEWAY_METHODS.PASSPORT_CONNECT;
    }

    @Override
    public Class<SessionActorLocal> getPropsClass() {
        return SessionActorLocal.class;
    }

    private static final class SessionActorLocal extends AbstractLocalServiceActor {

        public SessionActorLocal(Internal.EnumLite routerId) {
            super(routerId);
        }

        @Override
        public void onRequest(ApiRequest request) {
            ByteString tokenArg = request.getArgs(0);
            if (tokenArg == null) {
                // token 转换错误
                response(DefaultMessageProvider.produceMessage(BusinessMessage.error(ERROR_CODE.TOKEN_PARSE_ERROR)));
                return;
            }

//        String token = tokenArg.getAsString();
//        String parseToken = new String(BaseEncoding.base64Url().decode(token));
//        List<String> tokenChars = Splitter.on('|').splitToList(parseToken);
//        ContextAdapter.userLogin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
//        ContextAdapter.userLogin(context.channel().id(), tokenArg.getAsLong());

            long userId = Long.valueOf(tokenArg.toStringUtf8());
            getLogger().info("userId:{}, 登录成功", userId);
            getSender().tell(new LoginUserEntity(userId, DefaultMessageProvider.produceEmptyMessage()), getSelf());

            AkkaLocalWorkerSystem.INSTANCE.askLocalRouterNode(MemberOnlineEvent.newBuilder().setLoginUserId(userId).build());

        }
    }
}
