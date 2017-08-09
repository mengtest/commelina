package com.game.foundation.netty.test;

import com.game.foundation.netty.MessageAdapter;
import com.game.foundation.netty_spring.NettyNioSocketServerStarterForSpring;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

/**
 * Created by @panyao on 2017/8/4.
 */
public class SpringBootTest {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring-*.xml");
        NettyNioSocketServerStarterForSpring.startWithProtoBuff(context, 9002);

        MessageAdapter messageAdapter = context.getBean(MessageAdapter.class);

        Random random = new Random();
        random.setSeed(100);
        while (true) {
            if (random.nextInt() > 70) {
                MessageAdapter.MessageEntity entity = new MessageAdapter.MessageEntity<>();
                entity.setUserId(1);
                entity.setMsg("广播!");
                messageAdapter.addMessage(entity);
            }
            Thread.sleep(1111);
        }

    }

}