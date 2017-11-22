package com.commelina.server.passportv2.oauth2;

import com.commelina.web.mvc.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author panyao
 * @date 2017/11/21
 */
@Controller("/oauth2")
public class OAuth2Controller {

    @GetMapping("/open/id")
    public ResponseBodyMessage<String> getOpenId(@RequestParam String accessToken) {
        return null;
    }

    @GetMapping("/open/info")
    public ResponseBodyMessage<String> getOpenInfo(@RequestParam String accessToken) {
        return null;
    }

}
