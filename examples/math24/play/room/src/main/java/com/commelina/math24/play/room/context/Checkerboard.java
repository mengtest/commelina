package com.commelina.math24.play.room.context;

import akka.actor.Props;
import com.commelina.akka.cluster.nodes.AbstractServiceActor;
import com.commelina.math24.play.room.proto.Occupied;
import com.commelina.math24.play.room.proto.Prepare;
import com.commelina.math24.play.room.proto.Prepared;

/**
 * @author panyao
 * @date 2017/11/2
 * <p>
 * 0 | 0, 2, 3 ... sizeOf
 * 1 | sizeOf + 1, sizeOf + 2, sizeOf+ 3 ... sizeOf
 * ... | ....
 * sizeOf |
 * 0,1,2,3
 * 4,5,6,7
 * 8,9,10,11
 * 12,13,14,15
 * 设数组下标为 p, 棋盘宽度 sizeOf 为 c。 x、y 分表为棋盘的坐标。
 * <p>
 * eg1: 从一维数组找出二维数组坐标
 * <p>
 * p = c * x + y
 * <p>
 * =>
 * y = p % c
 * x = (p - y) / c;
 * <p>
 * eg2: 从一维数组找出二维数组坐标
 * <p>
 * p = c + 2 + y + x * (c + 1)
 * <p>
 * =>
 * <p>
 * y = (p - (c + 2)) % (c + 1)
 * x = (p - (c + 2) - y) / (c + 1)
 */
class Checkerboard extends AbstractServiceActor {

    private final int sizeX;
    private final int sizeY;

    private int[] values;

    public Checkerboard(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Occupied.class, o -> {
                    int occupiedSize = occupiedIndex(o.getX(), o.getY());
                    if (occupiedSize > 0) {
                        // 记录占领记录
                    }
                })
                .match(Prepare.class, p -> create())
                .build();
    }

    private int occupiedIndex(int x, int y) {
        try {
//            return value0[x][y]++;
            return 0;
        } catch (IndexOutOfBoundsException e) {
            // FIXME: 2017/11/2 返回客户端错误，非法的坐标
            return -1;
        }
    }

    private void create() {
        // FIXME: 2017/11/10 准备逻辑

        // 转变完成通知
        getSender().tell(Prepared.getDefaultInstance(), getSelf());
    }

    /**
     * 指定大小创建棋盘
     *
     * @param sizeX
     * @param sizeY
     * @return
     */
    public static Props props(int sizeX, int sizeY) {
        return Props.create(Checkerboard.class, sizeX, sizeY);
    }

}
