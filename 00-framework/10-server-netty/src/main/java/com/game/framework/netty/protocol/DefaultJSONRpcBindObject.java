package com.game.framework.netty.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.framework.netty.RPCRouterDispatchInterface;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by @panyao on 2017/8/7.
 */
public class DefaultJSONRpcBindObject implements RPCRouterDispatchInterface {

    private ObjectMapper objectMapper = new ObjectMapper();

    private EventLoop eventLoop = new DefaultEventLoop();

    private Map<String, Object> classes;

    @Override
    public final void invoke(ChannelHandlerContext ctx, Object jsonMessage) throws Exception {
        String message = (String) jsonMessage;
        if (Strings.isNullOrEmpty(message)) {
            throw new RuntimeException("Input message must be not null or empty.");
        }
        final RouterEntity entity;
        try {
            entity = objectMapper.readValue(message, RouterEntity.class);
        } catch (IOException e) {
            throw new RuntimeException("Input message type must be json.");
        }

        List<Callable<Object>> event = Lists.newArrayList();
        event.add(() -> invokeHandler(entity.getC(), entity.getM(), entity.getArgs()));
        List<Future<Object>> futures = eventLoop.invokeAll(event);
        Future<Object> apiHandlerResult = futures.get(0);
        if (apiHandlerResult.isDone()) {
            ChannelFuture future = ctx.writeAndFlush(apiHandlerResult.get());
            if (future.isDone()) {
                if (future.isSuccess()) {
                    // 成功
                } else if (future.cause() != null) {
                    // 异常
                    throw new Exception(future.cause());
                } else {
                    // 取消
                    throw new Exception("客户端取消执行");
                }
            }
        }
    }

    private Object invokeHandler(String className, String classMethod, Object[] args) {
        Object instance = classes.get(className);
        if (instance == null) {
            throw new RuntimeException("class not found.");
        }
        Method method;
        try {
            method = instance.getClass().getMethod(classMethod);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            if (args == null || args.length == 0) {
                return method.invoke(instance);
            }
            switch (args.length) {
                case 1:
                    return method.invoke(instance, args[0]);
                case 2:
                    return method.invoke(instance, args[0], args[1]);
                case 3:
                    return method.invoke(instance, args[0], args[1], args[2]);
                case 4:
                    return method.invoke(instance, args[0], args[1], args[2], args[3]);
                case 5:
                    return method.invoke(instance, args[0], args[1], args[2], args[3], args[4]);
                case 6:
                    return method.invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5]);
                case 7:
                    return method.invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                case 8:
                    return method.invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
                case 9:
                    return method.invoke(instance, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]);
                default:
                    throw new RuntimeException("参数不能超过9个");
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }

    // 加载配置文件
    public final void defaultSpringLoader(ApplicationContext context) {
        // 获取 beans
        classes = context.getBeansWithAnnotation(JSONRpc.class);
    }

}

class RouterEntity {
    private String c;
    private String m;
    private Object[] args;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}