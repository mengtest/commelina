package com.business.apis.pay.controller;


import com.github.freedompy.commelina.web.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品
 *
 * @author panyao
 * @date 2017/10/26
 */
@Controller
@RequestMapping("/pay/api/product")
public class Product {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> list() {

        return null;
    }

}
