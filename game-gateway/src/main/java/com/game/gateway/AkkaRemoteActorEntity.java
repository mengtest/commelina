package com.game.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by @panyao on 2017/9/6.
 */

@ConfigurationProperties(prefix = "akkaRemoteActor")
public class AkkaRemoteActorEntity {

    private String matchingPath;
    private String roomPath;

    public String getMatchingPath() {
        return matchingPath;
    }

    public void setMatchingPath(String matchingPath) {
        this.matchingPath = matchingPath;
    }

    public String getRoomPath() {
        return roomPath;
    }

    public void setRoomPath(String roomPath) {
        this.roomPath = roomPath;
    }
}
