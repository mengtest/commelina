package com.framework.message;

/**
 * Created by @panyao on 2017/9/6.
 */
public class ServerRouterMessage {

    private final ApiRequestLogin apiRequestLogin;

    private ServerRouterMessage(ApiRequestLogin apiRequestLogin) {
        this.apiRequestLogin = apiRequestLogin;
    }

    public static ServerRouterMessage newServerRouterMessage(ApiRequestLogin apiRequestLogin) {
        return new ServerRouterMessage(apiRequestLogin);
    }


    public ApiRequestLogin getApiRequestLogin() {
        return apiRequestLogin;
    }


}
