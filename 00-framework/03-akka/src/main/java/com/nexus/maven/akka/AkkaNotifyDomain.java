package com.nexus.maven.akka;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by @panyao on 2017/8/14.
 * <p>
 * 每个人对应不同的消息
 */
public class AkkaNotifyDomain {

    private final List<MessageEntity> messages = Lists.newArrayList();

    private AkkaNotifyDomain() {

    }

    public static AkkaNotifyDomain newNotify(long userId, Object message) {
        AkkaNotifyDomain notify = new AkkaNotifyDomain();
        return notify.addMessage(userId, message);
    }

    public AkkaNotifyDomain addMessage(long userId, Object message) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setUserId(userId);
        messageEntity.setMessage(message);
        return this;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public static class MessageEntity {
        private long userId;
        private Object message;

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public Object getMessage() {
            return message;
        }

        public void setMessage(Object message) {
            this.message = message;
        }
    }
}