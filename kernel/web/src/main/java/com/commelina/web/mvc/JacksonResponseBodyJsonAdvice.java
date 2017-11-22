package com.commelina.web.mvc;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * Created by @panyao on 2016/8/22.
 *
 * @author panyao
 * @coding.net https://coding.net/u/pandaxia
 * @github https://github.com/freedompy
 */
@ControllerAdvice
public class JacksonResponseBodyJsonAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
                                           MethodParameter returnType, ServerHttpRequest request,
                                           ServerHttpResponse response) {

        ((ResponseBodyMessage) bodyContainer.getValue()).businessMsg = "xxxxxxxxxxxxxx";
    }

}
