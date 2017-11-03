package com.business.apis.passport.controller;

import com.business.service.uc.service.CaptchaService;
import com.game.app.apis.proto.PASSPORT_ERROR_CODE;
import com.github.freedompy.commelina.webmvc.ResponseBodyMessage;
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
@RequestMapping("/passport/api/captcha")
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
            return ResponseBodyMessage.error(PASSPORT_ERROR_CODE.INPUT_TELEPHONE_FORMAT_ERROR);
        }

        captchaService.telephoneSms(tel);

        return ResponseBodyMessage.success();
    }

}
