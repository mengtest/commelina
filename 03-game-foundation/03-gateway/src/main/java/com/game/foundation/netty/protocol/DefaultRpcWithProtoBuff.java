package com.game.foundation.netty.protocol;

import com.game.foundation.netty.RPCRouterDispatchInterface;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.nexus.maven.utils.lang.Generator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.socket.netty.proto.SocketNettyProtocol;
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
public class DefaultRpcWithProtoBuff implements RPCRouterDispatchInterface {

    private final EventLoop eventLoop = new DefaultEventLoop();

    private Map<String, Object> classes;

    @Override
    public final void invoke(Long signInUserId, ChannelHandlerContext ctx, Object jsonMessage) throws Exception {
        String message = (String) jsonMessage;
        if (Strings.isNullOrEmpty(message)) {
            throw new RuntimeException("Input message must be not null or empty.");
        }
        final RouterEntity entity;
        try {
            entity = Generator.getJsonHolder().readValue(message, RouterEntity.class);
        } catch (IOException e) {
            throw new RuntimeException("Input message type must be json.");
        }

        List<Callable<String>> event = Lists.newArrayList();
        event.add(() -> invokeHandler(entity.getC(), entity.getM(), entity.getArgs()));
        // 这里使用事件循环的意思是一个用户的 channel 下的全部请求也会是有序的。
        List<Future<String>> futures = eventLoop.invokeAll(event);
        Future<String> apiHandlerResult = futures.get(0);
        if (apiHandlerResult.isDone()) {
            ChannelFuture future = ctx.writeAndFlush(apiHandlerResult.get());
            if (future.isDone()) {
                if (future.isSuccess()) {
                    // 成功
                } else if (future.cause() != null) {
                    // FIXME: 2017/8/8 全部转换为领域模型
                    // 异常
                    throw new Exception(future.cause());
                } else {
                    // 取消
                    throw new Exception("客户端取消执行");
                }
            }
        }
    }

    private String invokeHandler(String className, String classMethod, Object[] args) {
        Object instance = classes.get(className);
        if (instance == null) {
            return ResponseMessage.errorString(SocketNettyProtocol.DEFAULT_CONSTANTS.RPC_TARGET_CLASS_NOT_FOUND.getNumber());
        }

        Method method;
        Class[] classArgs = new Class[1];
        try {
            method = instance.getClass().getMethod(classMethod);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        try {
            if (args == null || args.length == 0) {
                Object rs = method.invoke(instance);
                if (rs != null) {
                    Generator.getJsonHolder().writeValueAsString(this);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        return "{}";
    }

    // 加载配置文件
    public final void defaultSpringLoader(ApplicationContext context) {
        // 获取 beans
        classes = context.getBeansWithAnnotation(RpcAPI.class);
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