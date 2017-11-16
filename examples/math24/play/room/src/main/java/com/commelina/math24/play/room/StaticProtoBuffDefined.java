package com.commelina.math24.play.room;

import com.commelina.akka.dispatching.proto.ActorResponse;
import com.commelina.math24.play.room.proto.ERROR_CODE;

/**
 * @author panyao
 * @date 2017/11/16
 */
public interface StaticProtoBuffDefined {

    ActorResponse ROOM_NOT_FOUND = ActorResponse.newBuilder().setError(ERROR_CODE.ROOM_NOT_FOUND_VALUE).build();

}
