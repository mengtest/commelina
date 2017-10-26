package com.github.freedompy.commelina.akka.router;

/**
 * @author @panyao
 * @date 2017/10/11
 */
public interface MemberEvent {

    /**
     * 当用户上线时触发的事件
     *
     * @param logoutUserId
     */
    void onOnline(long logoutUserId);

    /**
     * 当用户离线的时候触发的事件
     *
     * @param logoutUserId
     */
    void onOffline(long logoutUserId);

}
