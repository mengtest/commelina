package com.game.framework.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * Created by @panyao on 2017/8/3.
 */
@Component
public class GameServerHandler extends ChannelHandlerAdapter {

    //当客户端连上服务器的时候会触发此函数
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("client:" + ctx.channel().id() + " join server");
    }

    //当客户端断开连接的时候触发函数
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("client:" + ctx.channel().id() + " leave server");
        //User.onlineUser.remove(LoginDispatch.getInstance().user);
    }

    //当客户端发送数据到服务器会触发此函数
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        /*SocketModel message = (SocketModel) msg;
        switch (message.getType()) {
        case TypeProtocol.TYPE_LOGIN:
            LoginDispatch.getInstance().dispatch(ctx, message);
            break;
        case TypeProtocol.TYPE_WIZARD:
            WizardDispatch.getInstance().dispatch(ctx, message);
            break;
        case TypeProtocol.TYPE_USER:
            UserDispatch.getInstance().dispatch(ctx, message);
            break;
        case TypeProtocol.TYPE_BATTLE:
            BattleDispatch.getInstance().dispatch(ctx, message);
        default:
            break;
        }
        /*<br>　　　　　　　　　　　　这里我先把代码注释掉，现在还没讲到
        //        */
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //cause.printStackTrace();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
