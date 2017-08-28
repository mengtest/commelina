package com.game.matching.portal;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.game.matching.service.Matching;
import com.nexus.maven.core.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/10.
 */
public class MatchingRouter extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    private final ActorRef matching = getContext().actorOf(Matching.props(), "matching");

//    http://doc.akka.io/docs/akka/current/java/actors.html#lifecycle-monitoring-aka-deathwatch
//    private final ActorRef lastSender = getContext().system().deadLetters();

    @Override
    public void preStart() throws Exception {
        log.info("MatchingRouter Application started");
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        log.info("MatchingRouter Application stopped");
        super.postStop();
    }

    @Override
    public Receive createReceive() {
//      [
//          0 => [ 0 => {},1 => {}, 2 => {} ],
//          1 => [ 0 => {},1 => {}, 2 => {} ],
//          2 => [ 0 => {},1 => {}, 2 => {} ],
//      ]
        return receiveBuilder()
                // 收到来自远程的请求调用，即 gateway 进行业务层路由转发
                .match(ApiRequest.class, this::routerAdapter)
                // 因为连接保持，这里通知信息直接通知回 gateway ，由 gateway 推送到客户端
                .match(Terminated.class, t -> true, t -> {
                    log.info("Terminated " + t.toString());
                })
                // FIXME: 2017/8/15 响应如何确认送达了呢？
                .matchAny(o -> log.info("MatchingRouter received unknown message" + o))
                .build();
    }

    private void routerAdapter(ApiRequest request) {
        switch (request.getApiName()) {
            // 加入匹配
            case "joinMatch":
                this.addMatching(request.getArgs());
                break;
            default:
                this.unhandled("type " + request.getApiName() + " undefined.");
                break;
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
        // 发送一个内部消息 到 匹配的 service 的队列里去
        matching.forward(new Matching.JOIN_MATCH(userId), getContext());
    }

    public static Props props() {
        return Props.create(MatchingRouter.class);
    }
}
