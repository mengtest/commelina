package com.framework.webmvc;

import com.google.common.base.Strings;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @panyao on 2016/6/7.
 */
public final class AuthenticatedApiInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private SessionHandler sessionHandler;

    /**
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader("authenticated-token");
        do {
            if (!Strings.isNullOrEmpty(token)) {
                SessionHandler.ValidTokenEntity entity = sessionHandler.validToken(token);
                if (entity.userId > 0) {
                    request.setAttribute("userId", entity.userId);
                    if (!token.equals(entity.newToken)) {
                        addLogin(entity.newToken, response);
                        break;
                    }
                }
            }
            addLogin(sessionHandler.initAnonymous(), response);
        } while (false);
        return true;
    }

    public static void addLogin(String token, HttpServletResponse response) {
        response.setHeader("authenticated-token", token);
    }

}