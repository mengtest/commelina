package com.commelina.mvc;

import com.google.common.base.Strings;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API 拦截器
 *
 * @author @panyao
 * @date 2016/6/7
 */
@RequestMapping
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
        String token = request.getHeader("authenticated-newToken");
        do {
            if (!Strings.isNullOrEmpty(token)) {
                SessionHandler.SessionTokenEntity sessionTokenEntity = sessionHandler.validToken(token);
                if (sessionTokenEntity != null) {
                    if (!Strings.isNullOrEmpty(sessionTokenEntity.newTokenEntity.newToken)) {
                        addLogin(request, response, sessionTokenEntity);
                    } else {
                        if (sessionTokenEntity.userId > 0) {
                            request.setAttribute(SessionHandler.ATTRIBUTE_USER_ID, sessionTokenEntity.userId);
                        }
                        request.setAttribute(SessionHandler.ATTRIBUTE_SID, sessionTokenEntity.newTokenEntity.sid);
                    }
                    break;
                }
            }
            anonymous(request, response, sessionHandler.initAnonymous());
        } while (false);
        return true;
    }

    public static void addLogin(HttpServletRequest request, HttpServletResponse response,
                                SessionHandler.SessionTokenEntity sessionTokenEntity) {
        request.setAttribute(SessionHandler.ATTRIBUTE_USER_ID, sessionTokenEntity.userId);
        anonymous(request, response, sessionTokenEntity.newTokenEntity);
    }

    private static void anonymous(HttpServletRequest request, HttpServletResponse response,
                                  SessionHandler.NewTokenEntity newTokenEntity) {
        request.setAttribute(SessionHandler.ATTRIBUTE_SID, newTokenEntity.sid);
        response.setHeader("authenticated-newToken", newTokenEntity.newToken);
    }

}