package com.game.matching.portal;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.nexus.maven.akka.AkkaRequest;
import org.junit.Test;

/**
 * Created by @panyao on 2017/8/15.
 */
public class MatchRouterActorTest {

    @Test
    public void testMatchingRuning() {
        ActorSystem system = ActorSystem.create("test");
        TestKit probe = new TestKit(system);

        ActorRef actorRef = system.actorOf(MatchingGroup.props());
        actorRef.tell(AkkaRequest.newRequest("joinMatch", 1), probe.getRef());

//        AkkaResponse response = probe.expectMsgClass(AkkaResponse.class);

//        assertEquals(OpCodeConstants.JOIN_SUCCESS_RESPONSE, response.getMessage().getOpCode());

        actorRef.tell(AkkaRequest.newRequest("joinMatch", 2), probe.getRef());

//        AkkaResponse response1 = probe.expectMsgClass(AkkaResponse.class);
//
//        assertEquals(OpCodeConstants.JOIN_SUCCESS_RESPONSE, response1.getMessage().getOpCode());

        actorRef.tell(AkkaRequest.newRequest("joinMatch", 3), probe.getRef());

//        AkkaResponse response2 = probe.expectMsgClass(AkkaResponse.class);
//
//        assertEquals(OpCodeConstants.JOIN_SUCCESS_RESPONSE, response2.getMessage().getOpCode());
    }

}