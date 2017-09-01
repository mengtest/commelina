package com.framework.webmvc;

import com.framework.utils.URLUtils;
import com.google.common.base.Strings;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @panyao on 2016/6/7.
 */
public final class AuthenticatedWebInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private SessionHandler sessionHandler;

    private static String domain;

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
        Breaking:
        do {
            for (Cookie cookie : request.getCookies()) {
                if ("sid".equals(cookie.getName())) {
                    if (!Strings.isNullOrEmpty(cookie.getValue())) {
                        SessionHandler.ValidTokenEntity entity = sessionHandler.validToken(cookie.getValue());
                        if (entity.userId > 0) {
                            request.setAttribute("userId", entity.userId);
                            if (!cookie.getValue().equals(entity.newToken)) {
                                this.addSessionCookie(entity.newToken, request, response);
                            }
                            break Breaking;
                        }
                    }
                    break;
                }
            }
            this.addSessionCookie(sessionHandler.iniAnonymous(), request, response);
        } while (false);
        return true;
    }

    private void addSessionCookie(String token, HttpServletRequest request, HttpServletResponse response) {
        Cookie session = new Cookie("authenticated-token", token);

        if (domain == null) {
            // passport.example.com -> example.com
            String pattenDomain = URLUtils.getDomain(request.getServerName());
            if (pattenDomain != null) {
                domain = "." + pattenDomain;
                session.setDomain(domain);
            }
        } else {
            session.setDomain(domain);
        }

        session.setPath("/");
        session.setHttpOnly(true);

        response.addCookie(session);
    }

}