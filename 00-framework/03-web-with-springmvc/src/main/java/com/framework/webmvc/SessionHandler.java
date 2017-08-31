package com.framework.webmvc;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @panyao on 2017/8/31.
 */
public interface SessionHandler {

    default long doLogin(HttpServletRequest request, HttpServletResponse response) {
        // 会话上下文
        String token = request.getHeader("Authenticated-token");
        if (Strings.isNullOrEmpty(token)) {
            Object sessionId = request.getSession().getAttribute("userId");
            if (sessionId == null) {
                return 0L;
            }
            return Long.valueOf(sessionId.toString());
        }
        return 0L;
    }


    default void doSignIn(long userId, HttpServletRequest request, HttpServletResponse response) {
        // 会话上下文
        request.getSession().setAttribute("userId", userId);
    }

}
