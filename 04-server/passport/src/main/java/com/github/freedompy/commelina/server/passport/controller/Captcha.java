package com.github.freedompy.commelina.server.passport.controller;

import com.github.freedompy.commelina.mvc.ResponseBodyMessage;
import com.github.freedompy.commelina.server.passport.proto.ERROR_CODE;
import com.github.freedompy.commelina.server.passport.service.CaptchaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *
 * @author @panyao
 * @date 2017/9/4
 */
@Controller
@RequestMapping("/api/captcha")
public class Captcha {

    @Resource
    private CaptchaService captchaService;

    /**
     *
     * @param tel
     * @return
     */
    @RequestMapping(value = "/sms", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage telephone(@RequestParam String tel) {
        if (!ParamValid.telephone(tel)) {
            return ResponseBodyMessage.error(ERROR_CODE.INPUT_TELEPHONE_FORMAT_ERROR);
        }

        captchaService.telephoneSms(tel);

        return ResponseBodyMessage.success();
    }

}
