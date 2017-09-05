package com.app.passport.service;

import com.framework.utils.RandomEnhanced;
import com.framework.data.RedisKvRepository;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/4.
 */
@Repository
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private RedisKvRepository kvRepository;

    private final RandomEnhanced telephoneRandom = new RandomEnhanced();

    private final Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Override
    public void telephoneSms(String tel) {
        final int code = logger.isDebugEnabled() ? 1234 : telephoneRandom.nextInt(1000, 9999);
        // TODO: 2017/9/5 发送短信
        kvRepository.put(tel, code, 30 * 60 * 1000L);
    }

    @Override
    public boolean validTelephoneCode(String tel, String code) {
        String cacheCode = kvRepository.getAsString(tel);
        if (Strings.isNullOrEmpty(cacheCode)) {
            return false;
        }
        kvRepository.remove(tel);
        return cacheCode.equals(code);
    }
}
