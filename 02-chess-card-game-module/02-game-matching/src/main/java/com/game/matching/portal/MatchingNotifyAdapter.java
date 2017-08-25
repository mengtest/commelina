package com.game.matching.portal;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.nexus.maven.core.message.ApiRequest;

/**
 * Created by @panyao on 2017/8/10.
 */
public class MatchingNotifyAdapter extends AbstractActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);


//    http://doc.akka.io/docs/akka/current/java/actors.html#lifecycle-monitoring-aka-deathwatch
//    private final ActorRef lastSender = getContext().system().deadLetters();

    @Override
    public void preStart() throws Exception {
        log.info("MatchingRequestRouter Application started");
        super.preStart();
    }

    @Override
    public void postStop() throws Exception {
        log.info("MatchingRequestRouter Application stopped");
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
                .match(ApiRequest.class, null)
                // 因为连接保持，这里通知信息直接通知回 gateway ，由 gateway 推送到客户端
                .match(Terminated.class, t -> true, t -> {
                    log.info("Terminated " + t.toString());
                })
                // FIXME: 2017/8/15 响应如何确认送达了呢？
                .matchAny(o -> log.info("MatchingRequestRouter received unknown message" + o))
                .build();
    }


    public static Props props() {
        return Props.create(MatchingNotifyAdapter.class);
    }
}
