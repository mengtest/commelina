package com.game.foundation.gateway.service;

import akka.actor.ActorSystem;
import org.springframework.stereotype.Component;

/**
 * Created by @panyao on 2017/8/14.
 */
@Component
public class AkkaRoom {
    private final ActorSystem roomSystem = ActorSystem.create("room");

}
