package com.framework.web;

import com.google.common.base.Strings;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (domain == null) {
            Pattern pattern = Pattern
                    .compile("[\\w-]+\\.(com|cn)\\b()*$");
            Matcher matcher = pattern.matcher(request.getServerName());
            if (matcher.find()) {
                String pregDomain = matcher.group();
                // a.cn
                if (pregDomain != null && pregDomain.trim().length() >= 4) {
                    domain = "." + pregDomain;
                }
            }
        }

        Breaking:
        do {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("sid".equals(cookie.getName())) {
                        if (!Strings.isNullOrEmpty(cookie.getValue())) {
                            SessionHandler.SessionTokenEntity sessionTokenEntity = sessionHandler.validToken(cookie.getValue());
                            if (sessionTokenEntity != null) {
                                if (!Strings.isNullOrEmpty(sessionTokenEntity.newTokenEntity.newToken)) {
                                    addLogin(request, response, sessionTokenEntity);
                                } else {
                                    if (sessionTokenEntity.userId > 0) {
                                        request.setAttribute(SessionHandler.ATTRIBUTE_USER_ID, sessionTokenEntity.userId);
                                    }
                                    request.setAttribute(SessionHandler.ATTRIBUTE_SID, sessionTokenEntity.newTokenEntity.sid);
                                }
                                break Breaking;
                            }
                        }
                        break;
                        // to @ line anonymous
                    }
                }
            }
            // @ line anonymous
            anonymous(request, response, sessionHandler.initAnonymous());
        } while (false);
        return true;
    }

    private static void addSessionCookie(String token, HttpServletResponse response) {
        Cookie session = new Cookie("sid", token);
        if (domain != null) {
            session.setDomain(domain);
        }
        session.setPath("/");
        session.setHttpOnly(true);

        response.addCookie(session);
    }

    public static void addLogin(HttpServletRequest request, HttpServletResponse response,
                                SessionHandler.SessionTokenEntity sessionTokenEntity) {
        request.setAttribute(SessionHandler.ATTRIBUTE_USER_ID, sessionTokenEntity.userId);
        anonymous(request, response, sessionTokenEntity.newTokenEntity);
    }

    private static void anonymous(HttpServletRequest request, HttpServletResponse response, SessionHandler.NewTokenEntity newTokenEntity) {
        request.setAttribute(SessionHandler.ATTRIBUTE_SID, newTokenEntity.sid);
        addSessionCookie(newTokenEntity.newToken, response);

    }


}