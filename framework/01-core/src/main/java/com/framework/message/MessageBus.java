package com.framework.message;

import java.io.IOException;

/**
 * Created by @panyao on 2017/8/15.
 */
public interface MessageBus {

    enum BusinessProtocol {
        JSON
    }

    BusinessProtocol getBp();

    byte[] getBytes() throws IOException;

}
