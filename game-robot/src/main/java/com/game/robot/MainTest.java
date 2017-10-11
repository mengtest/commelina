package com.game.robot;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by panyao on 2017/9/12.
 */
public class MainTest {
    public static void main(String[] args) {
        List list = new LinkedList();

        for (int i = 0; i < list.size(); i++) {
            System.out.printf("i" + i);
        }

        BiMap<Long, Long> testMap = HashBiMap.create(1);
        testMap.put(1l, 99l);
        testMap.put(2l, 99l);
        testMap.put(3l, 99l);

//        iii
//                ／／ 明天改一下循环这里的问题
    }
}
