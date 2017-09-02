package com.twofourpoints.room.portal;

import com.framework.core_akka.RouterActor;
import com.framework.core_message.ApiRequestWithActor;

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
