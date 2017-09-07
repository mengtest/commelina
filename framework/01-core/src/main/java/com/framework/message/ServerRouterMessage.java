package com.framework.message;

/**
 * Created by @panyao on 2017/9/6.
 */
public class ServerRouterMessage {

    private final ApiLoginRequest apiLoginRequest;

    private ServerRouterMessage(ApiLoginRequest apiLoginRequest) {
        this.apiLoginRequest = apiLoginRequest;
    }

    public static ServerRouterMessage newServerRouterMessage(ApiLoginRequest apiLoginRequest) {
        return new ServerRouterMessage(apiLoginRequest);
    }


    public ApiLoginRequest getApiLoginRequest() {
        return apiLoginRequest;
    }


}
