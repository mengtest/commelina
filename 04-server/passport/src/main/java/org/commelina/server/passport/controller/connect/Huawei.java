package org.commelina.server.passport.controller.connect;

import org.commelina.mvc.ResponseBodyMessage;
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
@RequestMapping("/api/connect/channel/huawei")
public class Huawei {

    @RequestMapping(value = "/valid", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBodyMessage<String> huawei(@RequestParam String accessToken) {
        // http://developer.huawei.com/consumer/cn/service/hms/catalog/huaweiid.html?page=hmssdk_huaweiid_devguide#从网关直接获取openId
        // // TODO: 2017/10/26 获取 open id
        return null;
    }

}
