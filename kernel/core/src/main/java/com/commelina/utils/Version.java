package com.commelina.utils;

import com.google.common.base.Splitter;

import java.util.List;

/**
 * @author panyao
 * @date 2017/11/23
 */
public class Version {

    /**
     * 1.0.0 -> 1 00 00
     * 2.3.4 -> 2 03 04
     * 2.3.40 -> 2 03 40
     * 2.4.40 -> 2 04 40
     * 20.3.40 -> 20 03 40
     *
     * @param version
     * @return
     */
    public static int create(String version) {
        List<String> items = Splitter.on('.').splitToList(version);
        if (items.size() > 3 || items.size() < 1) {
            throw new RuntimeException("仅支持99.99.99为最大版本号");
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).length() > 2) {
                throw new RuntimeException("仅支持99.99.99为最大版本号");
            }
            if (i > 0 && items.get(i).length() == 1) {
                buffer.append("0");
            }
            buffer.append(items.get(i));
        }
        return Integer.valueOf(buffer.toString());
    }

}
