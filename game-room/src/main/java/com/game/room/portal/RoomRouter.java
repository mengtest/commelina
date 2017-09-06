package com.game.room.portal;

import com.framework.akka.AbstractRouterActor;
import com.framework.akka.ApiRequestWithActor;
import com.framework.message.RequestArg;

/**
 * Created by @panyao on 2017/8/17.
 */
public class RoomRouter extends AbstractRouterActor {

    @Override
    public void onRequest(ApiRequestWithActor request) {
        RequestArg roomId = request.getArg(0);
        if(roomId == null){


            return;
        }

    }

    //      [
//          0 => [ 0 => {},1 => {}, 2 => {} ],
//          1 => [ 0 => {},1 => {}, 2 => {} ],
//          2 => [ 0 => {},1 => {}, 2 => {} ],
//      ]
}
