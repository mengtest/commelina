package com.app.passport.controller;

import com.app.passport.proto.ERROR_CODE_CONSTANTS;
import com.app.passport.service.CaptchaService;
import com.framework.web.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by @panyao on 2017/9/4.
 */
@Controller
@RequestMapping("/api/passport/captcha")
public class Captcha {

    @Resource
    private CaptchaService captchaService;

    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage telephone(@RequestParam String tel) {
        if (!ParamValid.telephone(tel)) {
            return ResponseBodyMessage.error(ERROR_CODE_CONSTANTS.INPUT_TELEPHONE_FORMAT_ERROR);
        }

        captchaService.telephoneSms(tel);

        return ResponseBodyMessage.success();
    }

}
