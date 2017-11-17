package com.game.matching.portal;

import org.junit.Test;

/**
 * Created by @panyao on 2017/8/15.
 */
public class MatchAbstractAbstractMatchServiceActorModeActorClientRouterActorTest {

    @Test
    public void testMatchingRun() throws Exception {
//        ActorSystem system = ActorSystem.create("test");
//        TestKit probe = new TestKit(system);
//        ActorRef actorRef = system.actorOf(MatchingReceiveRequestActor.props(configEntity));
//        actorRef.tell(ApiRequest.newRequest(MATCHING_METHODS.JOIN_MATCH_QUENE, "1.0.0", new RequestArg[]{
//                new RequestArg("1", RequestArg.DATA_TYPE.LONG),
//        }), probe.getRef());

//        MessageBody response = probe.expectMsgClass(MessageBody.class);
//        assertEquals(OpCodeConstants.JOIN_SUCCESS_RESPONSE, response.getBody().getOpCode());

//        AkkaBroadcast broadcast = probe.expectMsgClass(AkkaBroadcast.class);
//        assertEquals(Long.valueOf(1), broadcast.getUserIds().get(0));
//
////        Map<String, Integer> kvEntity;
////        try {
////            kvEntity = Generator.getJsonHolder().readValue(broadcast.getBody().getBytes(), HashMap.class);
////        } catch (IOException e) {
////            throw e;
////        }
////        assertEquals(Long.valueOf(1), kvEntity.get("matchUserCount"));
//
//        actorRef.tell(AkkaRequest.newRequest("joinMatch", 2), probe.getRef());
//
//        AkkaResponseMessage response1 = probe.expectMsgClass(AkkaResponseMessage.class);
//
//        assertEquals(OpCodeConstants.JOIN_SUCCESS_RESPONSE, response1.getBody().getOpCode());
//
//        AkkaBroadcast broadcast1 = probe.expectMsgClass(AkkaBroadcast.class);
//        assertEquals(Long.valueOf(2), broadcast1.getUserIds().get(1));
////        Map<String, Integer> kvEntity1;
////        try {
////            kvEntity1 = Generator.getJsonHolder().readValue(broadcast1.getBody().getBytes(), HashMap.class);
////        } catch (IOException e) {
////            throw e;
////        }
////        assertEquals(Long.valueOf(2), kvEntity1.get("matchUserCount"));
//
//        actorRef.tell(AkkaRequest.newRequest("joinMatch", 3), probe.getRef());
//
//        AkkaResponseMessage response2 = probe.expectMsgClass(AkkaResponseMessage.class);
//
//        assertEquals(OpCodeConstants.JOIN_SUCCESS_RESPONSE, response2.getBody().getOpCode());
//
//        AkkaBroadcast broadcast3 = probe.expectMsgClass(AkkaBroadcast.class);
//        assertEquals(Long.valueOf(3), broadcast3.getUserIds().get(2));
//        Map<String, Integer> kvEntity3;
//        try {
//            kvEntity3 = Generator.getJsonHolder().readValue(broadcast3.getBody().getBytes(), HashMap.class);
//        } catch (IOException e) {
//            throw e;
//        }
//        assertEquals(Long.valueOf(3),kvEntity3.get("matchUserCount"));

    }

}