package com.business.apis.pay.controller;

import com.github.freedompy.commelina.mvc.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 第三方支付平台创建预订订单
 *
 * @author @panyao
 * @date 2017/8/31
 */
@Controller
@RequestMapping("/pay/api/prepay")
public class Prepay {

    @RequestMapping(value = "/alipay", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> alipay() {

        return null;
    }

    @RequestMapping(value = "/wxpay", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> wxpay() {

        return null;
    }

    @RequestMapping(value = "/applepay", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> applepay() {

        return null;
    }

}
