package com.github.freedompy.commelina.webmvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.freedompy.commelina.utils.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author @panyao
 * @date 2016/8/19
 */
public final class ExceptionHandlerResolverWithJsonOutput extends ExceptionHandlerExceptionResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerResolverWithJsonOutput.class);

    @Resource(name = "apiMessageSourceFile")
    private MessageSource source;

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           HandlerMethod handlerMethod,
                                                           Exception exception) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        String bytes;
        if (LOGGER.isDebugEnabled()) {
            ResponseBodyMessage<Exception> message = ResponseBodyMessage.success(() -> ResponseBodyMessage.SERVER_ERROR, exception);
            try {
                bytes = Generator.getJsonHolder().writeValueAsString(message);
            } catch (JsonProcessingException e) {
                LOGGER.error("{}", e);
                bytes = ResponseBodyMessage.getServerErrorJson(source.getMessage(ResponseBodyMessage.SERVER_ERROR_STR, null, request.getLocale()));
                response.addHeader("debug-failed", "1");
            }
        } else {
            bytes = ResponseBodyMessage.getServerErrorJson(source.getMessage(ResponseBodyMessage.SERVER_ERROR_STR, null, request.getLocale()));
        }

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(bytes);
        } catch (IOException e) {
            LOGGER.error("{}", e);
        }

        return null;
//        return super.doResolveHandlerMethodException(request, response, handlerMethod, exception);
    }
}