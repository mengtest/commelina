package com.foundation.app.passport.controller;

import com.framework.webmvc.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by @panyao on 2017/9/1.
 */
@Controller
public class Index {

    @RequestMapping("/")
    @ResponseBody
    public ResponseBodyMessage home1() {
        return ResponseBodyMessage.success();
    }

//    @RequestMapping("/")
//    @ResponseBody
//    public Map<String, Object> home() {
//        return Maps.newHashMap();
//    }

}
