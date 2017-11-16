package com.commelina.core;

/**
 * 消息定义
 *
 * @author @panyao
 * @date 2017/8/15
 */
@Deprecated
public interface MessageBody {

    /**
     * 获取消息的字节数组，用于直接发送给客户端
     * 编码错误则抛出 异常
     *
     * @return
     * @throws RuntimeException
     */
    byte[] getBytes() throws RuntimeException;

}
