package com.framework.akka_cluster_router;

import akka.dispatch.OnFailure;
import akka.dispatch.OnSuccess;
import com.framework.message.ApiRequest;
import com.framework.message.ApiRequestLogin;
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

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by @panyao on 2017/9/25.
 */
public abstract class DefaultLocalActorRequestHandler implements RequestHandler, Router, ApplicationContextAware {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    @PostConstruct
    public final void registerActor() {
        Map<String, ServiceHandler> routers = context.getBeansOfType(ServiceHandler.class);
        for (ServiceHandler handler : routers.values()) {
            AkkaWorkerSystem.Holder.AKKA_WORKER_SYSTEM.localRouterRegister(new LocalRouterRegistrationEntity(handler.getRouterId()), handler.getProps());
        }
    }

    @Override
    public final void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    @Override
    public final void onRequest(ApiRequest request, ChannelHandlerContext ctx) {
        LocalRouterJoinEntity entity = beforeHook(request, ctx);
        if (entity == null) {
            return;
        }
        afterHook(request, ctx, entity);
    }

    protected void afterHook(ApiRequest request, ChannelHandlerContext ctx, LocalRouterJoinEntity entity) {
        // 转发到业务 actor 上去
        Future<Object> future = AkkaWorkerSystem.Holder.AKKA_WORKER_SYSTEM.routerLocalNodeAsk(entity);

        // actor 处理成功
        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object result) throws Throwable {
                ReplyUtils.reply(ctx, getRouterId(), request.getOpcode(), ResponseMessage.newMessage(((RouterResponseEntity) result).getMessage()));
            }
        }, AkkaWorkerSystem.Holder.AKKA_WORKER_SYSTEM.getSystem().dispatcher());

        future.onFailure(new OnFailure() {
            @Override
            public void onFailure(Throwable failure) throws Throwable {
                ReplyUtils.reply(ctx, SERVER_CODE.SERVER_ERROR);
                logger.error("actor return error.{}", failure);
            }
        }, AkkaWorkerSystem.Holder.AKKA_WORKER_SYSTEM.getSystem().dispatcher());
    }

    protected LocalRouterJoinEntity beforeHook(ApiRequest request, ChannelHandlerContext ctx) {
        return createNewJoinEntity(request, 0);
    }

    protected final LocalRouterJoinEntity createNewJoinEntity(ApiRequest request, long userId) {
        return new LocalRouterJoinEntity(getRouterId(), ApiRequestLogin.newRequest(userId, request.getOpcode(), request.getVersion(), request.getArgs()));
    }


}
