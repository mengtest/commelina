package com.app.client_app.controller;

import com.framework.web.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 * Created by @panyao on 2017/9/4.
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
        ConfigEntity config;
        VersionEntity version;
        ResourceEntity resource;
    }

    static class ConfigEntity {

    }

    static class VersionEntity {

    }

    static class ResourceEntity {

    }

}
