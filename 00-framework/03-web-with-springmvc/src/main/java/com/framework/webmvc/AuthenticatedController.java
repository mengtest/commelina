package com.framework.webmvc;

import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @panyao on 2017/8/31.
 */
public abstract class AuthenticatedController {

    @Resource
    private SessionHandler sessionHandler;

    @ModelAttribute
    public void session(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("userId", sessionHandler.doLogin(request, response));
    }

}
