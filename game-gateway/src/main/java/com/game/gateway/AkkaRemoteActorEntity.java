package com.game.gateway;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by @panyao on 2017/9/6.
 */

@ConfigurationProperties(prefix = "akkaRemoteActor")
public class AkkaRemoteActorEntity {

    private String matchingRequestPath;
    private String matchingNotifyPath;

    private String roomRequestPath;
    private String roomServerRequestPath;
    private String roomNotifyPath;

    public String getMatchingRequestPath() {
        return matchingRequestPath;
    }

    public void setMatchingRequestPath(String matchingRequestPath) {
        this.matchingRequestPath = matchingRequestPath;
    }

    public String getRoomRequestPath() {
        return roomRequestPath;
    }

    public void setRoomRequestPath(String roomRequestPath) {
        this.roomRequestPath = roomRequestPath;
    }

    public String getMatchingNotifyPath() {
        return matchingNotifyPath;
    }

    public void setMatchingNotifyPath(String matchingNotifyPath) {
        this.matchingNotifyPath = matchingNotifyPath;
    }

    public String getRoomServerRequestPath() {
        return roomServerRequestPath;
    }

    public void setRoomServerRequestPath(String roomServerRequestPath) {
        this.roomServerRequestPath = roomServerRequestPath;
    }

    public String getRoomNotifyPath() {
        return roomNotifyPath;
    }

    public void setRoomNotifyPath(String roomNotifyPath) {
        this.roomNotifyPath = roomNotifyPath;
    }
}
