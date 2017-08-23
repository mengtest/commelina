package com.nexus.maven.netty.socket.router;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.nexus.maven.core.message.MessageBus;
import com.nexus.maven.netty.socket.PipelineFuture;
import com.nexus.maven.netty.socket.RPCRouterDispatchInterface;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.socket.netty.proto.SocketNettyProtocol;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by @panyao on 2017/8/9.
 */
@Component
public class DefaultRpcWithProtoBuff implements RPCRouterDispatchInterface {

    private static final Logger LOGGER = Logger.getLogger(DefaultRpcWithProtoBuff.class.getName());

//    private final EventLoop eventLoop = new DefaultEventLoop();

    private Map<String, InvokeMethodEntity> apiClasses;

    @Override
    public final void invoke(ChannelHandlerContext ctx, Object jsonMessage) {
        // 协议格式错误
        if (!(jsonMessage instanceof SocketNettyProtocol.SocketASK)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.PROTOCOL_FORMAT_ERROR);
            return;
        }

        // rpc name 不能为空
        SocketNettyProtocol.SocketASK request = (SocketNettyProtocol.SocketASK) jsonMessage;
        String apiName = request.getApiName();
        if (Strings.isNullOrEmpty(apiName)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RPC_API_NAME_NOT_ALLOW_EMPTY);
            return;
        }

        String version = request.getVersion();
        // 版本不允许为空
        if (Strings.isNullOrEmpty(version)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RPC_API_VERSION_NOT_ALLOW_EMPTY);
            return;
        }

        String api = apiName.concat(version);

        InvokeMethodEntity entity = this.apiClasses.get(api);
        if (entity == null) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RPC_API_NOT_FOUND);
            return;
        }

        Object[] args = new Object[request.getArgsList().size()];
        args[0] = ctx;
        if (request.getArgsList().size() > 0) {
            for (int i = 1, size = request.getArgsList().size(); i <= size; i++) {
                SocketNettyProtocol.Arg arg = request.getArgsList().get(i);
                switch (arg.getDataType()) {
                    case INT:
                        args[i] = Integer.valueOf(arg.getValue().toString());
                        break;
                    case STRING:
                        args[i] = arg.getValue().toString();
                        break;
                    case BOOL:
                        args[i] = Boolean.valueOf(arg.getValue().toString());
                        break;
                    case LONG:
                        args[i] = Long.valueOf(arg.getValue().toString());
                        break;
                    case DOUBLUE:
                        args[i] = Double.valueOf(arg.getValue().toString());
                        break;
                    case FLOAT:
                        args[i] = Float.valueOf(arg.getValue().toString());
                        break;
                    default:
                        throw new IllegalArgumentException("undefined arg type " + arg.getDataType());
                }
            }
        }
        Object object;
        try {
            object = entity.method.invoke(entity.instance, args);
        } catch (IllegalAccessException e) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RPC_METHOD_ARG_ERROR);
            LOGGER.info(e.getMessage());
            return;
        } catch (InvocationTargetException e) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RPC_METHOD_NOT_FOUND);
            LOGGER.info(e.getMessage());
            return;
        } catch (Throwable throwable) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.SERVER_ERROR);
            throwable.printStackTrace();
            return;
        }

        if (!(object instanceof ResponseHandler)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.SERVER_ERROR);
            LOGGER.info("method return value must be implements " + ResponseHandler.class);
            return;
        }

        ResponseHandler messageResponseHandler = (ResponseHandler) object;
        MessageBus messageBus = messageResponseHandler.getMessage();
        byte[] bytes = messageBus.getBytes();
        if (bytes == null) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.SERVER_ERROR);
            return;
        }

        SocketNettyProtocol.SocketMessage socketMessage = SocketNettyProtocol.SocketMessage.newBuilder()
                .setCode(SocketNettyProtocol.SYSTEM_CODE_CONSTANTS.RESONSE_CODE)
                .setDomain(messageResponseHandler.getDomain())
                .setMsg(SocketNettyProtocol.BusinessMessage.newBuilder()
                        .setOpCode(messageBus.getOpCode())
                        .setVersion(messageBus.getVersion())
                        .setBp(SocketNettyProtocol.BusinessProtocol.forNumber(messageBus.getBp().ordinal()))
                        .setMsg(ByteString.copyFrom(bytes))
                ).build();

        if (messageResponseHandler.getListener() == null) {
            this.futureComp(ctx.writeAndFlush(socketMessage));
        } else {
            this.futureEvent(ctx.writeAndFlush(socketMessage), messageResponseHandler.getListener());
        }
    }

    private void channelFutureFlush(ChannelHandlerContext ctx, SocketNettyProtocol.SYSTEM_CODE_CONSTANTS error_code) {
        SocketNettyProtocol.SocketMessage message = SocketNettyProtocol.SocketMessage.newBuilder()
                .setCode(error_code).build();
        ChannelFuture future = ctx.writeAndFlush(message);
        this.futureComp(future);
    }

    private void futureComp(ChannelFuture future) {
        if (future.isDone()) {
            this.futureDoneEvent(future);
        } else {
            throw new RuntimeException("这里是调试的错误，发现了就改了。");
        }
    }

    private void futureEvent(ChannelFuture future, PipelineFuture pipelineFuture) {
        if (future.isDone()) {
            this.futureDoneEvent(future);
            pipelineFuture.call(future);
        } else {
            throw new RuntimeException("这里是调试的错误，发现了就改了。");
        }
    }

    private void futureDoneEvent(ChannelFuture future) {
        if (future.isSuccess()) {
            // 成功
        } else if (future.cause() != null) {
            // FIXME: 2017/8/8 全部转换为领域模型
            // 异常
            //  throw new Exception(future.cause());
            LOGGER.info(future.cause().getMessage());
        } else {
            // 取消
            //  throw new Exception("客户端取消执行");
            LOGGER.info("client cancel receive message.");
        }
    }

    // 加载配置文件
    public final void defaultSpringLoader(ApplicationContext context) {
        // 获取 beans
        Map<String, Object> apiClasses = context.getBeansWithAnnotation(RpcApi.class);

        Map<String, InvokeMethodEntity> newLinkedMap = Maps.newLinkedHashMap();
        // api, method, version
        for (Map.Entry<String, Object> stringObjectEntry : apiClasses.entrySet()) {
            RpcApi rpcApi = stringObjectEntry.getValue().getClass().getAnnotation(RpcApi.class);
            String apiName = !Strings.isNullOrEmpty(rpcApi.apiName()) ? rpcApi.apiName() : stringObjectEntry.getKey();
            for (Method method : stringObjectEntry.getValue().getClass().getMethods()) {
                RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);

                if (rpcMethod == null) {
                    continue;
                }

                if (Strings.isNullOrEmpty(rpcMethod.value()) || Strings.isNullOrEmpty(rpcMethod.version())) {
                    throw new IllegalArgumentException("RpcMethod value or version is not allow empty.");
                }

                String api = apiName + rpcMethod.value() + rpcMethod.version();

                InvokeMethodEntity entity = new InvokeMethodEntity();
                entity.instance = stringObjectEntry.getValue();
                entity.method = method;

                if (newLinkedMap.containsKey(api)) {
                    throw new IllegalArgumentException(api + " exists.");
                }
                newLinkedMap.put(api, entity);
            }
        }
        this.apiClasses = newLinkedMap;
    }

    private class InvokeMethodEntity {
        public Object instance;
        public Method method;
    }

}

//        List<Callable<String>> event = Lists.newArrayList();
//        event.add(() -> invokeHandler(entity.getC(), entity.getM(), entity.getArgs()));
//        // 这里使用事件循环的意思是一个用户的 channel 下的全部请求也会是有序的。
//        List<Future<String>> futures = eventLoop.invokeAll(event);
//        Future<String> apiHandlerResult = futures.get(0);
//        if (apiHandlerResult.isDone()) {
//            ChannelFuture future = ctx.writeAndFlush(apiHandlerResult.get());
//            if (future.isDone()) {
//                if (future.isSuccess()) {
//                    // 成功
//                } else if (future.cause() != null) {
//                    // FIXME: 2017/8/8 全部转换为领域模型
//                    // 异常
//                    throw new Exception(future.cause());
//                } else {
//                    // 取消
//                    throw new Exception("客户端取消执行");
//                }
//            }
//        }