package com.game.foundation.gateway.portal;

import akka.actor.Props;
import com.game.foundation.gateway.MessageProvider;
import com.game.foundation.gateway.constants.DomainConstants;
import com.game.foundation.gateway.constants.OpCodeConstants;
import com.google.common.base.Splitter;
import com.google.common.io.BaseEncoding;
import com.nexus.maven.core.message.RequestArg;
import com.nexus.maven.netty.socket.*;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
@ActorWithApiController
public class GatewayConnectActorWithApiRemote implements ActorWithApiRemote {

    @Override
    public String getApiName() {
        return "/gateway/connect/v1.0.0";
    }

    @Override
    public Props getProps(ActorResponseContext context) {
        return ActorAkkaContext.RequestActor.props(DomainConstants.DOMAIN_GATE_WAY.ordinal(), context, request -> {
            RequestArg tokenArg = request.getArg(0);
            if (tokenArg == null) {
                // FIXME: 2017/8/25 null 处理
            }
            String token = tokenArg.getAsString();
            String parseToken = new String(BaseEncoding.base64Url().decode(token));
            List<String> tokenChars = Splitter.on('|').splitToList(parseToken);

            ContextAdapter.userJoin(context.getRawContext().channel().id(), Long.valueOf(tokenChars.get(0)));
            return MessageProvider.newMessage(OpCodeConstants.PASSPORT_CONNECT);
        });
    }

}
