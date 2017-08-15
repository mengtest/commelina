package com.nexus.maven.netty.socket;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by @panyao on 2017/8/11.
 */
@Component
public class MessageAdapter {

    private final MessageNotifyHandlerWithProtoBuff notifyHandler = new MessageNotifyHandlerWithProtoBuff();

    @Resource
    private NettyServerContext context;

    @PostConstruct
    public void init() {
        notifyHandler.context = context;
    }

    public void addNotify(NotifyResponseHandler messageHandler) throws IOException {
        notifyHandler.addNotify(messageHandler);
    }

}