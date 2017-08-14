package com.game.matching.portal;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.game.matching.service.MatchingActor;
import com.nexus.maven.akka.AkkaNotifyDomain;
import com.nexus.maven.akka.AkkaRequestDomain;
import com.nexus.maven.akka.AkkaResponseDomain;

/**
 * Created by @panyao on 2017/8/10.
 */
public class MatchRouterActor extends UntypedActor {

    // 匹配的接收者
    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof AkkaRequestDomain) {
            AkkaRequestDomain domain = (AkkaRequestDomain) o;
            switch (domain.getTypeRouter()) {
                // 加入匹配
                case "joinMatch":
                    this.addMatching(domain.getArgs());
                    break;
                default:
                    this.unhandled("type " + domain.getTypeRouter() + " undefined.");
                    break;
            }
        } else if (o instanceof MatchingActor.MSG) {
            MatchingActor.MSG msg = (MatchingActor.MSG) o;
            switch (msg) {
                case MATCHING_ROUTER:
                    // 收到匹配 service 业务处理成功的消息，通知到 gateway 的调用者
                    getSender().tell(AkkaResponseDomain.success(), getSelf());
                    break;
            }
        } else if (o instanceof AkkaNotifyDomain) {
            getSender().tell(AkkaResponseDomain.success(), getSelf());
        } else {
            this.unhandled(o);
        }
    }

    private void addMatching(Object[] args) {
        if (args == null || args.length <= 0) {
            this.unhandled("input args error.");
            return;
        }
        Object firstArg = args[0];
        if (firstArg == null) {
            this.unhandled("input fist arg must be not allow empty.");
            return;
        }

        long userId;
        try {
            userId = Long.valueOf(firstArg.toString());
        } catch (NumberFormatException e) {
            this.unhandled("parse first arg to long error.");
            return;
        }

        final ActorRef matchingActor = getContext().actorOf(Props.create(MatchingActor.class), "matchingActor");
        // 发送一个内部消息 到 匹配的 service 的队列里去
        matchingActor.tell(userId, getSelf());
    }

}
