package com.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by panyao on 2017/4/5.
 */
public final class URLUtils {

    /**
     * 从 url 里面提取 domain
     * passport.example.com -> example.com
     * https://www.oschina.net/code/snippet_856051_54123
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        Pattern pattern = Pattern
                .compile("[\\w-]+\\.(com|net|org|cn|gov|cc|biz|info|cn|co|com.cn|net.cn|gov.cn|org.cn)\\b()*$");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            String domain = matcher.group();
            // a.b
            if (domain != null && domain.trim().length() >= 3) {
                return domain;
            }
        }
        return null;
    }

}
