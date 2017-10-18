package com.game.room.service

import java.util

import akka.actor.AbstractActor
import com.framework.akka.router.cluster.nodes.AbstractServiceActor
import com.game.room.entity.PlayerEntity
import com.game.room.event.PlayerStatusEvent

/**
  * @author panyao
  * @date 2017/10/18
  */
class RoomContext(roomId: Long, playerEntities: util.List[PlayerEntity]) extends AbstractServiceActor {

  override def createReceive(): AbstractActor.Receive = {
    case PlayerStatusEvent => getLogger.info("received test")
  }

}
