package com.game.foundation.gateway.api;

import com.game.foundation.gateway.service.AkkaMatching;
import com.nexus.maven.netty.socket.router.ResponseHandler;
import com.nexus.maven.netty.socket.router.RpcApi;
import com.nexus.maven.netty.socket.router.RpcMethod;
import io.netty.channel.ChannelHandlerContext;
import scala.concurrent.Future;

import javax.annotation.Resource;
/**
 * Created by @panyao on 2017/8/9.
 */
@RpcApi
public class ActorRouterWithAkka {

    @Resource
    private AkkaMatching matching;

    @RpcMethod(value = "matching")
    public ResponseHandler routing(ChannelHandlerContext ctx, String funcName, String jsonArgs) {
        Future<ResponseHandler> future = matching.handler(funcName, null);
        // 这里把 akka 的错误处理了再返回
        return null;
    }

    @RpcMethod(value = "room")
    public void room(ChannelHandlerContext ctx) {

    }

}