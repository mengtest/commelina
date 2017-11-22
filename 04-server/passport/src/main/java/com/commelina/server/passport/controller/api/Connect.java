package com.commelina.server.passport.controller.api;

import com.commelina.server.passport.entity.MemberEntity;
import com.commelina.server.passport.proto.ERROR_CODE;
import com.commelina.server.passport.service.AccountService;
import com.commelina.server.passport.service.CaptchaService;
import com.commelina.utils.ServiceDomainMessage;
import com.commelina.web.mvc.ResponseBodyMessage;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author @panyao
 * @date 2017/8/31
 */
@RestController
@RequestMapping("/api/connect")
public class Connect {

    @Resource
    private AccountService accountService;

    @Resource
    private CaptchaService captchaService;

    /**
     * 手机免密登录
     *
     * @param tel
     * @param smsCode
     * @return
     */
    @RequestMapping(value = "/telwithvalidcode", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> withCode(@RequestParam String tel, @RequestParam int smsCode) {
        if (!ParamValid.telephone(tel)) {
            return ResponseBodyMessage.error(ERROR_CODE.INPUT_TELEPHONE_FORMAT_ERROR);
        }

        if (!captchaService.validTelephoneCode(tel, smsCode)) {
            return ResponseBodyMessage.error(ERROR_CODE.INPUT_SMS_CODE_ERROR);
        }

        ServiceDomainMessage<MemberEntity> message = accountService.singInWithTelOrNoPassword(tel);
        if (message.isSuccess()) {

            return ResponseBodyMessage.success();
        }

        return ResponseBodyMessage.error(message.getErrorCode());
    }

    @RequestMapping(value = "/telwithpass", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> withPassword(@RequestParam String tel, @RequestParam String pwd) {

        return null;
    }

}
