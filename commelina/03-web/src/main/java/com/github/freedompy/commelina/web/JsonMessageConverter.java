package com.github.freedompy.commelina.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * Created by @panyao on 2016/4/25.
 * http://jackyrong.iteye.com/blog/2247621
 */
public class JsonMessageConverter extends MappingJackson2HttpMessageConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonMessageConverter.class);

    /**
     * @param object
     * @param outputMessage
     * @throws IOException
     * @throws HttpMessageNotWritableException
     */
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        if (LOGGER.isDebugEnabled()) {
            if (!(object instanceof ResponseBodyMessage)) {
                throw new IOException("response message must be instanceof " + ResponseBodyMessage.class);
            }
        }
        super.writeInternal(object, outputMessage);
    }
}
