package com.game.framework.netty.test;

import com.game.framework.netty.MessageAdapter;
import com.game.framework.netty.MessageEntity;
import com.game.framework.netty.SpringGameServerStarter;
import com.game.framework.netty.protocol.DefaultJSONRpcBindObject;
import com.google.common.base.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

/**
 * Created by @panyao on 2017/8/4.
 */
public class SpringBootTest {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring-*.xml");
        DefaultJSONRpcBindObject defaultJSONRpcBindObject = new DefaultJSONRpcBindObject();
        defaultJSONRpcBindObject.defaultSpringLoader(context);

        SpringGameServerStarter.start(context, 9002, (token) -> {
            if (Strings.isNullOrEmpty(token)) {
                return false;
            }
            return true;
        }, defaultJSONRpcBindObject);

        MessageAdapter messageAdapter = context.getBean(MessageAdapter.class);

        Random random = new Random();
        random.setSeed(100);
        while (true) {
            if (random.nextInt() > 70) {
                MessageEntity entity = new MessageEntity();
                entity.setUserId(1);
                entity.setMsg("广播!");
                messageAdapter.addMessage(entity);
            }
            Thread.sleep(1111);
        }

    }

}