package com.framework.niosocket;

/**
 * Created by @panyao on 2017/8/28.
 */
public interface RouterEventHandler {

    /**
     * 用户上线
     *
     * @param ctx
     */
    default void onOnline(ChannelContextOutputHandler ctx) {

    }

    /**
     * 用户离线
     *
     * @param logoutUserId
     * @param ctx
     */
    default void onOffline(long logoutUserId, ChannelContextOutputHandler ctx) {

    }

    /**
     * 异常
     *
     * @param ctx
     * @param cause
     */
    default void onException(ChannelContextOutputHandler ctx, Throwable cause) {
        throw new RuntimeException(cause);
    }

}
