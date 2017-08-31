package com.framework.webmvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.framework.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by @panyao on 2016/8/19.
 */

public final class JsonExceptionHandlerResolver extends ExceptionHandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonExceptionHandlerResolver.class);

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           HandlerMethod handlerMethod,
                                                           Exception exception) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        ResponseBodyMessage message;
        if (LOGGER.isDebugEnabled()) {
            message = ResponseBodyMessage.error(() -> ResponseBodyMessage.SERVER_ERROR);
        } else {
            message = ResponseBodyMessage.success(() -> ResponseBodyMessage.SERVER_ERROR, exception);
        }

        PrintWriter printWriter;
        try {
            printWriter = response.getWriter();
        } catch (IOException e) {
            LOGGER.error("{}", e);
            return null;
        }

        String bytes;
        try {
            bytes = Generator.getJsonHolder().writeValueAsString(message);
        } catch (JsonProcessingException e) {
            LOGGER.error("{}", e);
            printWriter.print("{\"businessCode\":-1,\"data\":\"unknown error.\"}");
            return null;
        }

        printWriter.print(bytes);
        return null;
//        return super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
    }
}