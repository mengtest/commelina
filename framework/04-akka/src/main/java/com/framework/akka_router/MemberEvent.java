package com.framework.akka_router;

/**
 * Created by @panyao on 2017/10/11.
 */
public interface MemberEvent {

    void onOnline(long logoutUserId);

    void onOffline(long logoutUserId);

}