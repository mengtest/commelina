package com.commelina.server.passport.service;

import com.google.common.hash.Hashing;

/**
 * @author @panyao
 * @date 2017/9/4
 */
class PwdUtils {

    /**
     * 长度 64 位
     * @param pwd
     * @return
     */
    static String createPwd(String pwd) {
        return Hashing.sha256().hashUnencodedChars(pwd).toString();
    }

}
