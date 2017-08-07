package com.game.framework.nettytest;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by @panyao on 2017/8/4.
 */
@Component
public class GameEventContext {

    public static void main(String[] args) {

        EventLoop eventLoop = new DefaultEventLoop();

        ScheduledFuture<Long> future1 = eventLoop.schedule(() -> System.nanoTime(), 20 * 1000, TimeUnit.MICROSECONDS);

        List<Callable<Integer>> list = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            final int i_copy = i;
            list.add(() -> {
                System.out.println(i_copy + "thread:" + Thread.currentThread().getId());

                return i_copy;
            });
        }

        List<Future<Integer>> futures;
        long start_time = System.nanoTime();
        try {
            futures = eventLoop.invokeAll(list);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Future<Integer> future : futures) {
            try {
                System.out.println(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println("start time:" + System.currentTimeMillis());
        System.out.println("exe time:" + (System.nanoTime() - start_time));
    }

    public void test1() {
        EventLoop eventLoop = new DefaultEventLoop();


        for (int i = 0; i < 100; i++) {
            final int i_copy = i;
            eventLoop.execute(() -> {
                System.out.println("i:" + i_copy + ",thread:" + Thread.currentThread().getId());
            });
        }

        System.out.println("xx");
    }

    public void test() {

    }

}
