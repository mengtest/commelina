package com.commelina.server.release.controller;

import com.commelina.web.mvc.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 *
 * @author @panyao
 * @date 2017/9/4
 */
@Controller
@RequestMapping("/api/client/app")
public class Boot {

    @RequestMapping("/boot")
    @ResponseBody
    public ResponseBodyMessage<BootEntity> boot(@RequestHeader("cli-version") String version) {

        return null;
    }

    static class BootEntity implements Serializable {
        boolean config;
        boolean version;
        boolean resource;
    }

}
