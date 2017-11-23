package com.commelina.server.passportv2.controller.connect;

import com.commelina.server.passport.proto.ERROR_CODE;
import com.commelina.server.passportv2.service.CaptchaService;
import com.commelina.web.mvc.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.validation.constraints.AssertTrue;

/**
 *
 * @author @panyao
 * @date 2017/9/4
 */
@Controller
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Resource
    private CaptchaService captchaService;

    /**
     *
     * @param tel
     * @return
     */
    @GetMapping(value = "/sms")
    @ResponseBody
    public ResponseBodyMessage telephone(@RequestParam String tel,
                                         @AssertTrue String xx) {
        if (!ParamValid.telephone(tel)) {
            return ResponseBodyMessage.error(ERROR_CODE.INPUT_TELEPHONE_FORMAT_ERROR);
        }

        captchaService.telephoneSms(tel);

        return ResponseBodyMessage.success();
    }

}
