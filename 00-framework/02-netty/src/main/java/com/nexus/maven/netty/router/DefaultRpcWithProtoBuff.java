package com.nexus.maven.netty.router;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.protobuf.ByteString;
import com.nexus.maven.netty.RPCRouterDispatchInterface;
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

    private Map<String, InvokeMethodEntity> classes;

    @Override
    public final void invoke(ChannelHandlerContext ctx, Object jsonMessage) {

        // 协议格式错误
        if (!(jsonMessage instanceof SocketNettyProtocol.SocketASK)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.PROTOCOL_FORMAT_ERROR_VALUE);
            return;
        }

        // rpc name 不能为空
        SocketNettyProtocol.SocketASK request = (SocketNettyProtocol.SocketASK) jsonMessage;
        String className = request.getRcn();
        if (Strings.isNullOrEmpty(className)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_CLASS_NOT_ALLOW_EMPTY_VALUE);
            return;
        }

        String methodName = request.getRcm();
        // 方法名不允许为空
        if (Strings.isNullOrEmpty(methodName)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_CLASS_METHOD_NOT_ALLOW_EMPTY_VALUE);
            return;
        }

        String version = request.getRcmv();
        // 版本不允许为空
        if (Strings.isNullOrEmpty(version)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_CLASS_METHOD_VERSION_NOT_ALLOW_EMPTY_VALUE);
            return;
        }

        String api = className + methodName + version;

        InvokeMethodEntity entity = this.classes.get(api);
        if (entity == null) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_API_NOT_FOUND_VALUE);
            return;
        }

        Object object;
        if (request.getArgsList().size() == 0) {
            try {
                object = entity.method.invoke(entity.instance);
            } catch (IllegalAccessException e) {
                this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_METHOD_ARG_ERROR_VALUE);
                LOGGER.info(e.getMessage());
                return;
            } catch (InvocationTargetException e) {
                this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_METHOD_NOT_FOUND_VALUE);
                LOGGER.info(e.getMessage());
                return;
            } catch (Throwable throwable) {
                this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_SERVER_ERROR_VALUE);
                throwable.printStackTrace();
                return;
            }
        } else {
            Object[] args = new Object[request.getArgsList().size()];

            for (int i = 0, size = request.getArgsList().size(); i < size; i++) {
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

            try {
                object = entity.method.invoke(entity.instance, args);
            }  catch (IllegalAccessException e) {
                this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_METHOD_ARG_ERROR_VALUE);
                LOGGER.info(e.getMessage());
                return;
            } catch (InvocationTargetException e) {
                this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_METHOD_NOT_FOUND_VALUE);
                LOGGER.info(e.getMessage());
                return;
            } catch (Throwable throwable) {
                this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_SERVER_ERROR_VALUE);
                throwable.printStackTrace();
                return;
            }
        }

        if (!(object instanceof ResponseHandler)) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_SERVER_ERROR_VALUE);
            LOGGER.info("method return value must be implements " + ResponseHandler.class);
            return;
        }

        ResponseHandler responseHandler = (ResponseHandler) object;

        byte[] bytes = responseHandler.getBytes();
        if (bytes == null) {
            this.channelFutureFlush(ctx, SocketNettyProtocol.SYSTEM_ERROR_CONSTANTS.RPC_SERVER_ERROR_VALUE);
            return;
        }

        SocketNettyProtocol.SocketMessage socketMessage = SocketNettyProtocol.SocketMessage.newBuilder()
                .setMsg(
                        SocketNettyProtocol.BusinessMessage.newBuilder()
                                .setOpCode(responseHandler.getOpCode())
                                .setMsg(ByteString.copyFrom(bytes))
                ).build();

        if (responseHandler.getListener() == null) {
            this.futureComp(ctx.writeAndFlush(socketMessage));
        } else {
            this.futureEvent(ctx.writeAndFlush(socketMessage), responseHandler.getListener());
        }
    }

    private void channelFutureFlush(ChannelHandlerContext ctx, int error_code) {
        SocketNettyProtocol.SocketMessage message = SocketNettyProtocol.SocketMessage.newBuilder()
                .setCode(error_code).build();
        ChannelFuture future = ctx.writeAndFlush(message);
        this.futureComp(future);
    }

    private void futureComp(ChannelFuture future) {
        if (future.isDone()) {
            this.futureDoneEvent(future);
        }
    }

    private void futureEvent(ChannelFuture future, PipelineFuture pipelineFuture) {
        if (future.isDone()) {
            this.futureDoneEvent(future);
            pipelineFuture.call(future);
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
            for (Method method : stringObjectEntry.getValue().getClass().getMethods()) {
                RpcMethod rpcMethod = method.getAnnotation(RpcMethod.class);
                if (rpcMethod == null) {
                    continue;
                }
                if (Strings.isNullOrEmpty(rpcMethod.value()) || Strings.isNullOrEmpty(rpcMethod.version())) {
                    throw new IllegalArgumentException("RpcMethod value or version is not allow empty.");
                }

                String api = stringObjectEntry.getKey() + rpcMethod.value() + rpcMethod.version();

                InvokeMethodEntity entity = new InvokeMethodEntity();
                entity.instance = stringObjectEntry.getValue();
                entity.method = method;

                if (newLinkedMap.containsKey(api)) {
                    throw new IllegalArgumentException(api + " exists.");
                }
                newLinkedMap.put(api, entity);
            }
        }
        this.classes = newLinkedMap;
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