package org.commelina.akka.dispatching.cluster.nodes;

import akka.actor.AbstractActor;
import akka.actor.Address;
import akka.actor.Terminated;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.commelina.akka.dispatching.proto.ApiRequestForward;
import org.commelina.akka.dispatching.DispatchForward;
import org.commelina.akka.dispatching.MemberEvent;
import org.commelina.akka.dispatching.cluster.Constants;
import org.commelina.akka.dispatching.proto.ApiRequest;
import org.commelina.akka.dispatching.proto.MemberOfflineEvent;
import org.commelina.akka.dispatching.proto.MemberOnlineEvent;
import org.commelina.core.MessageBody;

/**
 * @author @panyao
 * @date 2017/9/25
 */
public abstract class BackendActor extends AbstractActor implements DispatchForward, MemberEvent {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), getClass());

    private final Cluster cluster = Cluster.get(getContext().system());
    private Address frontendAddress;

    //subscribe to cluster changes, MemberUp
    @Override
    public void preStart() {
        cluster.subscribe(self(), ClusterEvent.MemberUp.class, ClusterEvent.MemberRemoved.class);
    }

    //re-subscribe when restart
    @Override
    public void postStop() {
        cluster.unsubscribe(self());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ApiRequest.class, this::onRequest)
                .match(ApiRequestForward.class, this::onForward)
                .match(MemberOfflineEvent.class, off -> onOffline(off.getLogoutUserId()))
                .match(MemberOnlineEvent.class, on -> onOnline(on.getLoginUserId()))

                .match(ClusterEvent.CurrentClusterState.class, state -> {
                    for (Member member : state.getMembers()) {
                        if (member.status().equals(MemberStatus.up())) {
                            register(member);
                        } else if (member.status().equals(MemberStatus.removed())) {
                            remove(member);
                        }
                    }
                })
                .match(ClusterEvent.MemberUp.class, mUp -> register(mUp.member()))
                .match(ClusterEvent.MemberRemoved.class, mRem -> remove(mRem.member()))
                .match(Terminated.class, t -> {
                    logger.info("Frontend {} left.", getSender());
//                    getContext().unwatch(getSender());
//                    ClusterChildNodeSystem.INSTANCE.removeRouterFronted();
                })
                .build();
    }

    @Override
    public void onOnline(long logoutUserId) {
        // nothing to do
    }

    @Override
    public void onOffline(long logoutUserId) {
        // nothing to do
    }

    @Override
    public void onForward(ApiRequestForward forward) {
        // nothing to do
    }

    protected void response(MessageBody message) {
        getSender().tell(message, getSelf());
    }

    void register(Member member) {
        if (member.hasRole(Constants.CLUSTER_FRONTEND)) {
            logger.info("Frontend port:{} , nodes register.", member.address().port().get());
//            getContext().watch(getSender());
            ClusterChildNodeSystem.INSTANCE.registerRouterFronted(getSender());
        }
    }

    void remove(Member member) {
        if (member.hasRole(Constants.CLUSTER_FRONTEND)) {
            logger.info("Frontend port:{} , nodes remove.", member.address().port().get());
//            getContext().unwatch(getSender());
            ClusterChildNodeSystem.INSTANCE.removeRouterFronted();
//            frontendAddress = null;
        }
    }

    protected LoggingAdapter getLogger() {
        return logger;
    }

}
