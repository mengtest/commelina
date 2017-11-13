package com.commelina.akka.dispatching;

import com.commelina.akka.dispatching.proto.MemberOfflineEvent;
import com.commelina.akka.dispatching.proto.MemberOnlineEvent;

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
    void onOnline(MemberOnlineEvent logoutUserId);

    /**
     * 当用户离线的时候触发的事件
     *
     * @param logoutUserId
     */
    void onOffline(MemberOfflineEvent logoutUserId);

}
