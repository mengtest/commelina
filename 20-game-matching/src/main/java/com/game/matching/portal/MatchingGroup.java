package com.game.matching.portal;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.game.matching.service.Matching;
import com.nexus.maven.core.message.ApiRequest;
import com.nexus.maven.core.message.ApiResponse;
import com.nexus.maven.core.message.BroadcastResponse;

/**
 * Created by @panyao on 2017/8/10.
 */
public class MatchingGroup extends AbstractActor {

    public static final String GOURP_PATH = "user/";

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    public static Props props() {
        return Props.create(MatchingGroup.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 收到来自远程的请求调用，即 gateway 进行业务层路由转发
                .match(ApiRequest.class, r -> {
                    switch (r.getApiName()) {
                        // 加入匹配
                        case "joinMatch":
                            this.addMatching(r.getArgs());
                            break;
                        default:
                            this.unhandled("type " + r.getApiName() + " undefined.");
                            break;
                    }
                })
                // 业务层匹配回调，则把消息回传到请求的调用者, 由 gateway 推送到客户端
                .match(ApiResponse.class, r -> getSender().tell(r, getSelf()))
                // 因为连接保持，这里通知信息直接通知回 gateway ，由 gateway 推送到客户端
                .match(BroadcastResponse.class, b -> getSender().tell(b, getSelf()))
//                .matchAny(u -> unhandled(u))
                .build();
    }

    @Override
    public void preStart() {
        log.info("MatchingGroup Application started");
    }

    @Override
    public void postStop() {
        log.info("MatchingGroup Application stopped");
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

        final ActorRef matchingActor = getContext().actorOf(Matching.props(), "matchingActor");

        // 发送一个内部消息 到 匹配的 service 的队列里去
        matchingActor.tell(new Matching.JOIN_MATCH(userId), getSelf());
    }
}
