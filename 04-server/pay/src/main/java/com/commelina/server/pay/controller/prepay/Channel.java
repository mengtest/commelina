package com.commelina.server.pay.controller.prepay;

import com.commelina.mvc.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 创建渠道的支付订单
 *
 * @author @panyao
 * @date 2017/8/31
 */
@Controller
@RequestMapping("/pay/api/prepay/channel")
public class Channel {

    @RequestMapping(value = "/huawei", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> alipay() {

        return null;
    }

    @RequestMapping(value = "/passport", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> wxpay() {

        return null;
    }

}
