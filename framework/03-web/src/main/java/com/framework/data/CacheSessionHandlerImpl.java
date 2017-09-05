package com.framework.data;

import com.framework.utils.SnowflakeIdWorker;
import com.framework.web.SessionHandler;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/1.
 */
public class CacheSessionHandlerImpl implements SessionHandler {

    @Resource
    private CacheKvRepository cacheKvRepository;

    //  因为没有规模，所以这里直接写死, 否则也需要通过依赖注入实现
    private final SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);

    private final String prefix = "session:";

    @Override
    public ValidTokenEntity validToken(String token) {
        TokenEntity tokenEntity = TokenUtils.decodeToken(token);
        if (tokenEntity == null) {
            return null;
        }

        if (!tokenEntity.isValid()) {
            if (tokenEntity.getSid() > 0) {
                cacheKvRepository.remove("session:" + tokenEntity.getSid());
            }
            return null;
        }

        ValidTokenEntity validTokenEntity = new ValidTokenEntity();
        validTokenEntity.setUserId(tokenEntity.getUid());

        // 匿名用户处理
        if (tokenEntity.getUid() <= 0) {
            // 匿名用户不过期
            // TODO: 2017/9/5 待商榷
        } else {
            // 提前一天交换 token
            if (tokenEntity.getExpireTime() - 86400 * 1000 < System.currentTimeMillis()) {
                validTokenEntity.setNewToken(doSignIn(tokenEntity.getUid()));
                // 20s 后过期
                cacheKvRepository.expire(prefix + tokenEntity.getSid(), 20 * 1000L);
            }
        }

        return validTokenEntity;
    }

    @Override
    public String doSignIn(long userId) {
        // 7 天
        return createToken(userId, 7 * 86400 * 100);
    }

    @Override
    public String initAnonymous() {
        // 10 分钟
        return createToken(0, 10 * 60 * 1000);
    }

    private String createToken(long userId, long expireTtl) {
        long sid = snowflakeIdWorker.nextId();
        // 7 天过期
        String token = TokenUtils.encodeToken(userId, sid, expireTtl);
        cacheKvRepository.put(prefix + sid, userId, expireTtl);
        return token;
    }


}
