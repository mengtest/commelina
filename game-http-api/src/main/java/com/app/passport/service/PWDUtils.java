package com.app.passport.service;

import com.google.common.hash.Hashing;

/**
 * Created by @panyao on 2017/9/4.
 */
class PWDUtils {

    static String createPwd(String pwd) {
        return Hashing.sha256().hashUnencodedChars(pwd).toString();
    }

}
