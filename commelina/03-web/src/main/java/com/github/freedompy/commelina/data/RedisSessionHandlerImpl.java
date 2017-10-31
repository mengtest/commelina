package com.github.freedompy.commelina.data;

import com.github.freedompy.commelina.utils.SnowflakeIdWorker;
import com.github.freedompy.commelina.web.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author @panyao
 * @date 2017/9/1
 */
public class RedisSessionHandlerImpl implements SessionHandler {

    @Resource(name = "session")
    private RedisTemplate<String, Long> redisTemplate;

    /**
     * 因为没有规模，所以这里直接写死
     */
    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    /**
     * 因为没有规模，所以这里直接写死
     */
    private final SnowflakeIdWorker anonymousSnowflakeIdWorker = new SnowflakeIdWorker(0, 1);

    private static final String PREFIX = "session:";

    private static final Long SESSION_EXPIRE_TTL = 7 * 86400 * 1000L;
    private static final Long ANONYMOUS_EXPIRE_TTL = 5 * 60 * 1000L;

    private static final Long TOKEN_CHANGE_TIME = 60000L;

    private final Logger logger = LoggerFactory.getLogger(RedisSessionHandlerImpl.class);

    @Override
    public SessionTokenEntity validToken(String token) {
        TokenEntity tokenEntity = TokenUtils.decodeToken(token);
        if (tokenEntity == null) {
            return null;
        }

        // 如果存在 token 验证不过，那么就有被劫持的风险，所以需要令登录的用户 token 失效，引导用户创建新的token
        // 这里似乎还要给用户单独加上盐 list "sid, slat","sid, slat","sid, slat"
        if (!tokenEntity.isValid() || tokenEntity.getExpireTime() < System.currentTimeMillis()) {
            return null;
        }

        // 登录用户
        if (tokenEntity.getUid() > 0) {
            final Long sid = redisTemplate.opsForValue().get(PREFIX + tokenEntity.getUid());
            if (sid != tokenEntity.getSid()) {
                // 明天最好改成 list
                final long userId = redisTemplate.opsForValue().get(PREFIX + tokenEntity.getSid());
                if (userId <= 0 || userId != tokenEntity.getUid()) {
                    redisTemplate.delete(PREFIX + tokenEntity.getSid());
                    return null;
                }

                // 令被劫持的 uid 下的 token 失效
                if (System.currentTimeMillis() - tokenEntity.getExpireTime() - SESSION_EXPIRE_TTL > TOKEN_CHANGE_TIME) {
                    redisTemplate.delete(PREFIX + tokenEntity.getSid());
                    redisTemplate.delete(PREFIX + tokenEntity.getUid());
                    return null;
                }
                // 在此期间，两个 token 都有效
            } else {
                // token 快要过期就交换 token
                if (tokenEntity.getExpireTime() - TOKEN_CHANGE_TIME < System.currentTimeMillis()) {
                    return doSignIn(tokenEntity.getUid());
                }
            }
            SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
            sessionTokenEntity.setUserId(tokenEntity.getUid());
            NewTokenEntity newTokenEntity = new NewTokenEntity();
            newTokenEntity.setSid(sid);
            sessionTokenEntity.setNewTokenEntity(newTokenEntity);

            return sessionTokenEntity;
        } else {
            // 匿名用户 不需要验证
            if (tokenEntity.getExpireTime() - TOKEN_CHANGE_TIME < System.currentTimeMillis()) {
                return null;
            }
            return ANONYMOUS;
        }

    }

    @Override
    public SessionTokenEntity doSignIn(long userId) {
        long sid = snowflakeIdWorker.nextId();
        String token = TokenUtils.encodeToken(userId, sid, SESSION_EXPIRE_TTL);

        redisTemplate.multi();
        redisTemplate.opsForValue().getAndSet(PREFIX + userId, sid);
        redisTemplate.expire(PREFIX + userId, SESSION_EXPIRE_TTL, TimeUnit.MILLISECONDS);

        Object oldSid = redisTemplate.exec().stream().findFirst();
        if (oldSid != null) {
            logger.info("Old sid {}, new sid {}.", oldSid, sid);
            redisTemplate.opsForValue().set(PREFIX + sid, userId, TOKEN_CHANGE_TIME, TimeUnit.MILLISECONDS);
        }

        NewTokenEntity tokenEntity = new NewTokenEntity();
        tokenEntity.setNewToken(token);
        tokenEntity.setSid(sid);

        SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
        sessionTokenEntity.setUserId(userId);
        sessionTokenEntity.setNewTokenEntity(tokenEntity);
        return sessionTokenEntity;
    }

    @Override
    public NewTokenEntity initAnonymous() {
        long sid = anonymousSnowflakeIdWorker.nextId();
        String token = TokenUtils.encodeToken(0, sid, ANONYMOUS_EXPIRE_TTL);
        NewTokenEntity tokenEntity = new NewTokenEntity();
        tokenEntity.setNewToken(token);
        tokenEntity.setSid(sid);
        return tokenEntity;
    }

}