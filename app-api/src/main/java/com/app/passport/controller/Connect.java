package com.app.passport.controller;

import com.app.passport.entity.MemberEntity;
import com.app.passport.proto.ERROR_CODE_CONSTANTS;
import com.app.passport.service.AccountService;
import com.framework.utils.ServiceDomainMessage;
import com.framework.web.AuthenticatedApiInterceptor;
import com.framework.web.ResponseBodyMessage;
import com.framework.web.SessionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by @panyao on 2017/8/31.
 */
@Controller
@RequestMapping("/api/passport/connect")
public class Connect {

    @Resource
    private AccountService accountService;

    @Resource
    private SessionHandler sessionHandler;

    /**
     * 手机免密登录
     *
     * @param tel
     * @param smsCode
     * @param response
     * @return
     */
    @RequestMapping(value = "/nopasswordtel", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> telephone(@RequestParam String tel, @RequestParam int smsCode, HttpServletResponse response) {
        if (!ParamValid.telephone(tel)) {
            return ResponseBodyMessage.error(ERROR_CODE_CONSTANTS.INPUT_TELEPHONE_FORMAT_ERROR);
        }

        // INPUT_CODE_ERROR

//        if (pwd == null || pwd.length() < 6 || pwd.length() > 16) {
//            return ResponseBodyMessage.error(ERROR_CODE_CONSTANTS.INPUT_PWD_LENGTH_ERROR);
//        }

        ServiceDomainMessage<MemberEntity> message = accountService.singInWithTelAndNoPassword(tel);
        if (message.isSucess()) {
            String token = sessionHandler.doSignIn(message.getT().getUid());
            AuthenticatedApiInterceptor.addLogin(token, response);
            return ResponseBodyMessage.success();
        }

        return ResponseBodyMessage.error(message.getErrorCode());
    }

}
