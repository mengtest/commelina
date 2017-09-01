package com.foundation.app.passport.controller;

import com.framework.webmvc.ResponseBodyMessage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by @panyao on 2017/8/31.
 */
@RestController
@RequestMapping("/api/passport/connect")
public class Connect {

    @RequestMapping("/haochang/tel")
    public ResponseBodyMessage haochang() {

        return null;
    }

    @RequestMapping("/haochang/wx")
    public ResponseBodyMessage wx() {

        return null;
    }

}
