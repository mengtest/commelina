package com.github.freedompy.commelina.core;

import java.io.IOException;

/**
 * 消息定义
 *
 * @author @panyao
 * @date 2017/8/15
 */
public interface MessageBody {

    /**
     * 获取消息的字节数组，用于直接发送给客户端
     * 编码错误则抛出 异常
     *
     * @return
     * @throws IOException
     */
    byte[] getBytes() throws IOException;

}
