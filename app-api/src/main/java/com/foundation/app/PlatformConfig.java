package com.foundation.app;

import org.springframework.context.annotation.ImportResource;

/**
 * Created by @panyao on 2017/9/1.
 */
@ImportResource(locations = {
        "classpath:platform-api-spring-beans.xml",
        "classpath:platform-api-spring-mvc.xml",
})
public class PlatformConfig {

}
