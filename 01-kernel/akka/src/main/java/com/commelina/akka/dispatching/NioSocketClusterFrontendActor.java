package com.commelina.akka.dispatching;

import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.ActorBroadcast;
import com.commelina.akka.dispatching.proto.ActorNotify;
import com.commelina.akka.dispatching.proto.ActorWorld;
import com.commelina.niosocket.MessageAdapter;
import com.commelina.niosocket.message.BroadcastMessage;
import com.commelina.niosocket.message.NotifyMessage;
import com.commelina.niosocket.message.WorldMessage;

/**
 * @author panyao
 * @date 2017/11/16
 */
public class NioSocketClusterFrontendActor extends AbstractClusterFrontendActor {

    public NioSocketClusterFrontendActor(int domain, Timeout timeout) {
        super(domain, timeout);
    }

    @Override
    public void event(ActorNotify notify) {
        MessageAdapter.sendNotify(getDomain(), NotifyMessage.newMessage(
                notify.getOpcode(),
                notify.getUserId(),
                notify.getMessage()
        ));
    }

    @Override
    public void event(ActorBroadcast broadcast) {
        MessageAdapter.sendBroadcast(getDomain(), BroadcastMessage.newBroadcast(
                broadcast.getOpcode(), broadcast.getUserIdsList(), broadcast.getMessage()
        ));
    }

    @Override
    public void event(ActorWorld world) {
        MessageAdapter.sendWorld(getDomain(), WorldMessage.newMessage(
                world.getOpcode(), world.getMessage()));
    }
}
