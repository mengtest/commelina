package com.business.apis.passport.controller;

import com.business.service.uc.entity.MemberEntity;
import com.business.service.uc.service.AccountService;
import com.business.service.uc.service.CaptchaService;
import com.game.app.apis.proto.PASSPORT_ERROR_CODE;
import com.github.freedompy.commelina.utils.ServiceDomainMessage;
import com.github.freedompy.commelina.web.AuthenticatedApiInterceptor;
import com.github.freedompy.commelina.web.ResponseBodyMessage;
import com.github.freedompy.commelina.web.SessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author @panyao
 * @date 2017/8/31
 */
@Controller
@RequestMapping("/passport/api/connect")
public class Connect {

    @Resource
    private AccountService accountService;

    @Resource
    private SessionHandler sessionHandler;

    @Resource
    private CaptchaService captchaService;

    /**
     * 手机免密登录
     *
     * @param tel
     * @param smsCode
     * @param response
     * @return
     */
    @RequestMapping(value = "/telwithvalidcode", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> telephone(@RequestParam String tel, @RequestParam int smsCode,
                                                 HttpServletRequest request, HttpServletResponse response) {
        if (!ParamValid.telephone(tel)) {
            return ResponseBodyMessage.error(PASSPORT_ERROR_CODE.INPUT_TELEPHONE_FORMAT_ERROR);
        }

        if (!captchaService.validTelephoneCode(tel, smsCode)) {
            return ResponseBodyMessage.error(PASSPORT_ERROR_CODE.INPUT_SMS_CODE_ERROR);
        }

        ServiceDomainMessage<MemberEntity> message = accountService.singInWithTelAndNoPassword(tel);
        if (message.isSuccess()) {
            SessionHandler.SessionTokenEntity sessionTokenEntity = sessionHandler.doSignIn(message.getData().getUid());
            AuthenticatedApiInterceptor.addLogin(request, response, sessionTokenEntity);
            return ResponseBodyMessage.success();
        }

        return ResponseBodyMessage.error(message.getErrorCode());
    }

    @RequestMapping(value = "/telwithpass", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> telephone1(@RequestParam String tel, @RequestParam int smsCode,
                                                  HttpServletRequest request, HttpServletResponse response) {

        return null;
    }

}
