package com.twofourpoints.room.portal;

import com.nexus.maven.core.akka.RouterActor;
import com.nexus.maven.core.message.ApiRequestWithLogin;
import com.nexus.maven.core.message.MemberOfflineEvent;
import com.nexus.maven.core.message.MemberOnlineEvent;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomRouter extends RouterActor {

    @Override
    protected void onRequest(ApiRequestWithLogin request) {

    }

    @Override
    protected void onOnline(MemberOnlineEvent onlineEventWithLogin) {

    }

    @Override
    protected void onOffline(MemberOfflineEvent offlineEvent) {

    }

    //      [
//          0 => [ 0 => {},1 => {}, 2 => {} ],
//          1 => [ 0 => {},1 => {}, 2 => {} ],
//          2 => [ 0 => {},1 => {}, 2 => {} ],
//      ]
}
