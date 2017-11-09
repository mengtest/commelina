package com.business.apis.game.controller;

import org.commelina.mvc.ResponseBodyMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author @panyao
 * @date 2017/9/29
 */
@Controller
@RequestMapping("/api/game/room")
public class Room {

    @RequestMapping("/create")
    @ResponseBody
    public ResponseBodyMessage<String> boot() {

        return null;
    }

}