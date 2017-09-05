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

    private static final Long TOKEN_CHANGE_TIME = 60000L;

    private final Logger logger = LoggerFactory.getLogger(CacheSessionHandlerImpl.class);

    // FIXME: 2017/9/5 两个问题，1 token 需要改成list维护 2 sessionHandler 还需要打磨

    @Override
    public SessionTokenEntity validToken(String token) {
        TokenEntity tokenEntity = TokenUtils.decodeToken(token);
        if (tokenEntity == null) {
            return null;
        }

        // 如果存在 token 验证不过，那么就有被劫持的风险，所以需要令登录的用户 token 失效，引导用户创建新的token
        // 这里似乎还要给用户单独加上盐 list "sid, slat","sid, slat","sid, slat"
        if (!tokenEntity.isValid() || tokenEntity.getExpireTime() < System.currentTimeMillis()) {
            if (tokenEntity.getUid() > 0) {
                cacheKvRepository.remove(prefix + tokenEntity.getUid());
            }
            return null;
        }

        SessionTokenEntity sessionTokenEntity = new SessionTokenEntity();
        // 登录用户
        if (tokenEntity.getUid() > 0) {
            final long sid = cacheKvRepository.getAsLong(prefix + tokenEntity.getUid());
            if (sid != tokenEntity.getSid()) {
                // 明天最好改成 list
                final long userId = cacheKvRepository.getAsLong(prefix + tokenEntity.getSid());
                if (userId <= 0 || userId != tokenEntity.getUid()) {
                    cacheKvRepository.remove(prefix + tokenEntity.getSid());
                    return null;
                }

                // 令被劫持的 uid 下的 token 失效
                if (System.currentTimeMillis() - tokenEntity.getExpireTime() - SESSION_EXPIRE_TTL > TOKEN_CHANGE_TIME) {
                    cacheKvRepository.remove(prefix + tokenEntity.getSid());
                    cacheKvRepository.remove(prefix + tokenEntity.getUid());
                    return null;
                }
                // 在此期间，两个 token 都有效
            } else {
                // token 快要过期就交换 token
                if (tokenEntity.getExpireTime() - TOKEN_CHANGE_TIME < System.currentTimeMillis()) {
                    sessionTokenEntity.setNewToken(doSignIn(tokenEntity.getUid()));
                }
            }
            sessionTokenEntity.setUserId(tokenEntity.getUid());
        } else {
            // 匿名用户 不需要验证
            if (tokenEntity.getExpireTime() - TOKEN_CHANGE_TIME < System.currentTimeMillis()) {
                return null;
            }
        }

        return sessionTokenEntity;
    }

    @Override
    public String doSignIn(long userId) {
        final long sid = snowflakeIdWorker.nextId();
        String token = TokenUtils.encodeToken(userId, sid, SESSION_EXPIRE_TTL);
        // FIXME: 2017/9/5 这里明天要改成一个用户持有一个 list 才对，
        // 交换 token ，永远只保留最新的一个 token
        long oldSid = cacheKvRepository.getAndSet(prefix + userId, sid, SESSION_EXPIRE_TTL);
        if (oldSid > 0) {
            logger.info("Old sid {}, new sid {}.", oldSid, userId);
            cacheKvRepository.put(prefix + sid, userId, TOKEN_CHANGE_TIME);
        }
        return token;
    }

    @Override
    public String initAnonymous() {
        return TokenUtils.encodeToken(0, anonymousSnowflakeIdWorker.nextId(), ANONYMOUS_EXPIRE_TTL);
    }

}