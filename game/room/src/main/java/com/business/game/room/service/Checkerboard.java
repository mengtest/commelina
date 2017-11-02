package com.business.game.room.service;

import com.game.room.proto.Occupied;
import com.github.freedompy.commelina.akka.dispatching.cluster.nodes.AbstractServiceActor;

/**
 * @author panyao
 * @date 2017/11/2
 */
public class Checkerboard extends AbstractServiceActor {

    /**
     * [x][y] = x
     */
    private int[][] value0 = new int[100][100];
    private int[][] value1 = new int[100][100];
    private int[][] value2 = new int[100][100];

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Occupied.class, o -> {
                    int occupiedSize = occupiedIndex(o.getX(), o.getY());
                    if (occupiedSize > 0) {
                        // 记录占领记录
                    }
                })
                .build();
    }

    private int occupiedIndex(int x, int y) {
        try {
            return value0[x][y]++;
        } catch (IndexOutOfBoundsException e) {
            // FIXME: 2017/11/2 返回客户端错误，非法的坐标
            return -1;
        }
    }

}
