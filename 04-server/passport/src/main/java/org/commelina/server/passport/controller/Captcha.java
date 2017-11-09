package org.commelina.server.passport.controller;

import org.commelina.server.passport.service.CaptchaService;
import org.commelina.mvc.ResponseBodyMessage;
import org.commelina.server.passport.proto.ERROR_CODE;
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
