package com.commelina.akka.dispatching;

import akka.util.Timeout;
import com.commelina.akka.dispatching.proto.ActorBroadcast;
import com.commelina.akka.dispatching.proto.ActorDebugMessage;
import com.commelina.akka.dispatching.proto.ActorNotify;
import com.commelina.akka.dispatching.proto.ActorWorld;
import com.commelina.niosocket.MessageAdapter;

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
        MessageAdapter.sendNotify(getDomain(), notify.getOpcode(), notify.getUserId(), notify.getMessage());
    }

    @Override
    public void event(ActorBroadcast broadcast) {
        MessageAdapter.sendBroadcast(getDomain(), broadcast.getOpcode(), broadcast.getUserIdsList(), broadcast.getMessage());
    }

    @Override
    public void event(ActorWorld world) {
        MessageAdapter.sendWorld(getDomain(), world.getOpcode(), world.getMessage());
    }

    @Override
    protected void remoteDebug(ActorDebugMessage message) {
        MessageAdapter.sendError(getDomain(), message.getUserIdsList(), message.getMessage());
    }
}
