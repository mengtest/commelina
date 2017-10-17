package com.framework.data;

import com.google.common.base.Strings;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author @panyao
 * @date 2017/9/4
 */
public class RedisCacheKvRepositoryImpl implements CacheKvRepository {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void put(String k, long v, Long pTtl) {
        stringRedisTemplate.opsForValue().set("kv:" + k, v + "", pTtl, TimeUnit.MICROSECONDS);
    }

    @Override
    public void put(String k, int v, Long pTtl) {
        put(k, (long) v, pTtl);
    }

    @Override
    public long getAndSet(String k, Long v, Long pTtl) {
        stringRedisTemplate.multi();
        stringRedisTemplate.opsForValue().getAndSet("kv" + k, v + "");
        stringRedisTemplate.expire("kv" + k, pTtl, TimeUnit.MICROSECONDS);
        List<Object> execResult = stringRedisTemplate.exec();
        if (execResult.get(0) == null) {
            return 0;
        }
        return Long.valueOf(execResult.get(0).toString());
    }

    @Override
    public void remove(String k) {
        stringRedisTemplate.delete("kv:" + k);
    }

    @Override
    public long getAsLong(String k) {
        String val = this.getAsString(k);
        if (Strings.isNullOrEmpty(val)) {
            return 0;
        }
        return Long.valueOf(val);
    }

    @Override
    public int getAsInt(String k) {
        String val = this.getAsString(k);
        if (Strings.isNullOrEmpty(val)) {
            return 0;
        }
        return Integer.valueOf(val);
    }

    @Override
    public String getAsString(String k) {
        return stringRedisTemplate.opsForValue().get("kv:" + k);
    }

}
