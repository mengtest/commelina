package com.business.service.passport.service;

import com.google.common.hash.Hashing;

/**
 * @author @panyao
 * @date 2017/9/4
 */
class PwdUtils {

    static String createPwd(String pwd) {
        return Hashing.sha256().hashUnencodedChars(pwd).toString();
    }

}
