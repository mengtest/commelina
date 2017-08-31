package com.framework.webmvc;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @panyao on 2017/8/31.
 */
@ControllerAdvice(annotations = {RestController.class, ResponseBody.class})
public class RestControllerExceptionTranslator {

}
