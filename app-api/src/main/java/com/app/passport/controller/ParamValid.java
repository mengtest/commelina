package com.app.passport.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by @panyao on 2017/9/4.
 */
class ParamValid {

    private static final Pattern telephonePattern = Pattern.compile("^1[345789]\\d{9}$");

    static boolean telephone(String tel) {
        Matcher matcher = telephonePattern.matcher(tel);
        if (matcher.find()) {
            String matchedTelephone = matcher.group();
            // a.cn
            if (matchedTelephone != null && matchedTelephone.trim().length() == 11) {
                return true;
            }
        }
        return false;
    }
}
