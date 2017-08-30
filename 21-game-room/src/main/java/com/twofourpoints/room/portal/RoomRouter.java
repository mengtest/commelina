package com.twofourpoints.room.portal;

import com.nexus.maven.core.akka.RouterActor;
import com.nexus.maven.core.message.ApiRequestWithActor;
import com.nexus.maven.core.message.MemberOfflineEvent;
import com.nexus.maven.core.message.MemberOnlineEvent;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomRouter extends RouterActor {

    @Override
    public void onRequest(ApiRequestWithActor request) {

    }

    //      [
//          0 => [ 0 => {},1 => {}, 2 => {} ],
//          1 => [ 0 => {},1 => {}, 2 => {} ],
//          2 => [ 0 => {},1 => {}, 2 => {} ],
//      ]
}
