package com.framework.akka_router;

import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import com.framework.message.ApiRequest;
import com.framework.message.ResponseMessage;
import com.framework.niosocket.ReplyUtils;
import com.framework.niosocket.RequestHandler;
import com.framework.niosocket.proto.SERVER_CODE;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import scala.concurrent.Future;

import java.util.Map;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class DefaultLocalActorRequestHandler implements RequestHandler, Router, ApplicationContextAware {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, ServiceHandler> routers = applicationContext.getBeansOfType(ServiceHandler.class);
        for (ServiceHandler handler : routers.values()) {
            AkkaWorkerSystem.Holder.WORKER.localRouterRegister(new LocalRouterRegistrationEntity(handler.getRouterId()), handler.getProps());
        }
    }

    @Override
    public void onRequest(ApiRequest request, ChannelHandlerContext ctx) {
        afterHook(request, ctx, beforeHook(request, ctx));
    }

    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx, LocalRouterJoinEntity entity) {
        // 转发到业务 actor 上去
        Future<Object> future = AkkaWorkerSystem.Holder.WORKER.routerLocalNodeAsk(entity);

        // actor 处理成功
        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(),
                        ResponseMessage.newMessage(((RouterResponseEntity) result).getMessage()));
            }
        }, AkkaWorkerSystem.Holder.WORKER.getSystem().dispatcher());

        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable failure) throws Throwable {
                ReplyUtils.reply(ctx, SERVER_CODE.SERVER_ERROR);
                logger.error("actor return error.{}", failure);
            }
        }, AkkaWorkerSystem.Holder.WORKER.getSystem().dispatcher());
    }

    protected LocalRouterJoinEntity beforeHook(ApiRequest request, ChannelHandlerContext ctx) {
        return new LocalRouterJoinEntity(getRouterId(), request);
    }

}