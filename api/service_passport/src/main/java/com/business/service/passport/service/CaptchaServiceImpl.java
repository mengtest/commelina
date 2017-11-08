package com.business.service.passport.service;

import com.github.freedompy.commelina.utils.RandomEnhanced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author @panyao
 * @date 2017/9/4
 */
@Repository
public class CaptchaServiceImpl implements CaptchaService {

    private final RandomEnhanced telephoneRandom = new RandomEnhanced();

    private final Logger logger = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Override
    public void telephoneSms(String tel) {
        final int code = logger.isDebugEnabled() ? 1234 : telephoneRandom.nextInt(1000, 9999);
        // TODO: 2017/9/5 发送短信
//        cacheKvRepository.put(tel, code, 30 * 60 * 1000L);
    }

    @Override
    public boolean validTelephoneCode(String tel, int code) {
        if (code <= 0) {
            return false;
        }
//        final int cacheCode = cacheKvRepository.getAsInt(tel);
//        // 验证成功，移除 code
//        if (Integer.valueOf(code).equals(cacheCode)) {
//            cacheKvRepository.remove(tel);
//            return true;
//        }
        return false;
    }
}
