package com.framework.data;

import com.framework.utils.SnowflakeIdWorker;
import com.framework.web.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/1.
 */
public class CacheSessionHandlerImpl implements SessionHandler {

    @Resource
    private CacheKvRepository cacheKvRepository;

    //  因为没有规模，所以这里直接写死, 否则也需要通过依赖注入实现
    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    private final SnowflakeIdWorker anonymousSnowflakeIdWorker = new SnowflakeIdWorker(0, 1);

    private final String prefix = "session:";

    private static final Long SESSION_EXPIRE_TTL = 7 * 86400 * 1000L;
    private static final Long ANONYMOUS_EXPIRE_TTL = 5 * 60 * 1000L;

    private static final int tokenChangeTime = 60000;

    private final Logger logger = LoggerFactory.getLogger(CacheSessionHandlerImpl.class);

    @Override
    public ValidTokenEntity validToken(String token) {
        TokenEntity tokenEntity = TokenUtils.decodeToken(token);
        if (tokenEntity == null) {
            return null;
        }

        // 如果存在 token 验证不过，那么就有被劫持的风险，所以需要令登录的用户 token 失效，引导用户创建新的token
        if (!tokenEntity.isValid() || tokenEntity.getExpireTime() < System.currentTimeMillis()) {
            if (tokenEntity.getUid() > 0) {
                cacheKvRepository.remove(prefix + tokenEntity.getUid());
            }
            return null;
        }

        ValidTokenEntity validTokenEntity = new ValidTokenEntity();
        // 登录用户
        if (tokenEntity.getUid() > 0) {
            final long sid = cacheKvRepository.getAsLong(prefix + tokenEntity.getUid());
            if (sid != tokenEntity.getSid()) {
                // 令被劫持的 uid 下的 token 失效
                if (System.currentTimeMillis() - tokenEntity.getExpireTime() - SESSION_EXPIRE_TTL > tokenChangeTime) {
                    cacheKvRepository.remove(prefix + tokenEntity.getUid());
                    return null;
                }
                // 在此期间，两个 token 都有效
            } else {
                // token 快要过期就交换 token
                if (tokenEntity.getExpireTime() - tokenChangeTime < System.currentTimeMillis()) {
                    validTokenEntity.setNewToken(doSignIn(tokenEntity.getUid()));
                }
            }
            validTokenEntity.setUserId(tokenEntity.getUid());
        } else {
            // 匿名用户
            if (tokenEntity.getExpireTime() - tokenChangeTime < System.currentTimeMillis()) {
                validTokenEntity.setNewToken(refreshAnonymousToken(tokenEntity.getSid()));
            }
        }

        return validTokenEntity;
    }

    @Override
    public String doSignIn(long userId) {
        final long sid = snowflakeIdWorker.nextId();
        String token = TokenUtils.encodeToken(userId, sid, SESSION_EXPIRE_TTL);
        long oldSid = cacheKvRepository.getAndSet(prefix + userId, sid, SESSION_EXPIRE_TTL);
        if (oldSid > 0) {
            logger.info("Old sid {}, new sid {}.", oldSid, userId);
        }
        return token;
    }

    @Override
    public String initAnonymous() {
        return refreshAnonymousToken(anonymousSnowflakeIdWorker.nextId());
    }

    private String refreshAnonymousToken(long sid) {
        String token = TokenUtils.encodeToken(0, sid, ANONYMOUS_EXPIRE_TTL);
        cacheKvRepository.put(prefix + sid, 0, ANONYMOUS_EXPIRE_TTL);
        return token;
    }

}