package com.instruction.matching.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import com.instruction.matching.apis.ApiDef;
import com.instruction.matching.service.Matching;
import com.nexus.maven.core.akka.RouterActor;
import com.nexus.maven.core.message.ApiRequestWithLogin;

/**
 * Created by @panyao on 2017/8/29.
 */
public class MatchingRouter extends RouterActor {

    private final ActorRef matching = getContext().actorOf(Matching.props(), "matching");

    @Override
    protected void onRequest(ApiRequestWithLogin request) {
        switch (Integer.valueOf(request.getApiName())) {
            case ApiDef.MATCHING_APIS.JOIN_MATCH_QUENE_VALUE:
                matching.tell(new Matching.JOIN_MATCH(request.getUserId()), this.getSelf());
                break;
            // FIXME: 2017/8/29 处理 接口不存在的情况
        }
    }

    public static Props props() {
        return Props.create(MatchingRouter.class);
    }

}
