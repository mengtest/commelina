package com.framework.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * Created by @panyao on 2016/8/22.
 */
@Configuration
public class ResponseBodyJsonAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseBodyJsonAdvice.class);

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {
        if (LOGGER.isDebugEnabled()) {
            if (!(bodyContainer.getValue() instanceof ResponseBodyMessage)) {
                throw new RuntimeException("response message must be instanceof " + ResponseBodyMessage.class);
            }
        }
    }

}
