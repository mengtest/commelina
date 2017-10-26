package com.app.passport.controller.connect;

import com.github.freedompy.commelina.web.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author panyao
 * @date 2017/10/25
 */
@Controller
@RequestMapping("/passport/api/connect/party")
public class Party {

    @RequestMapping(value = "/huawei", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> huawei(@RequestParam String tel) {

        return null;
    }

    @RequestMapping(value = "/uc", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> uc(@RequestParam String tel) {

        return null;
    }

}
