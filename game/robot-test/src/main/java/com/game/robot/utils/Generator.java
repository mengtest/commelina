package com.game.robot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Created by @panyao on 2016/4/27.
 *
 * @author panyao
 */
public final class Generator {

    private Generator() {
    }

    /**
     * json帮助类
     *
     * @return
     */
    public static ObjectMapper getJsonHolder() {
        return JsonHolder.objectMapper;
    }

    /**
     * --------------------------------------------分割线---------------------------------------------------------------
     */
    private static class JsonHolder {

        private static ObjectMapper objectMapper = new ObjectMapper() {
            {
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//                configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);


//                setDateFormat (new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"));
//                // 空值处理为空串
//                getSerializerProvider ().setNullValueSerializer (new JsonSerializer<Object> () {
//
//                    @Override
//                    public void serialize (
//                            Object value,
//                            JsonGenerator jg,
//                            SerializerProvider sp) throws IOException, JsonProcessingException {
//                        jg.writeString ("");
//                    }
//                });
            }
        };
    }

}
