package com.framework.niosocket.akka;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ChannelContextOutputHandler;

/**
 * Created by @panyao on 2017/8/29.
 */
@Deprecated
public abstract class RequestHandler extends AbstractActor  {

    private final int domain;
    protected final ChannelContextOutputHandler context;

    public RequestHandler(int domain, ChannelContextOutputHandler context) {
        this.domain = domain;
        this.context = context;
    }

    public AbstractActor.Receive createReceive() {
        return receiveBuilder()
                // 请求事件
                .match(ApiRequest.class, this::onRequest)
                .build();
    }

    public abstract  void  onRequest(ApiRequest request);

    public final void reply(ResponseMessage message) {
        context.reply(domain, message);
    }

    public static Props props(Class<? extends RequestHandler> clazz, int domain, ChannelContextOutputHandler context) {
        return Props.create(clazz, domain, context);
    }

}