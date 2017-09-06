package com.framework.message;

import com.framework.akka.ApiRequestWithActor;

/**
 * Created by @panyao on 2017/9/6.
 */
public class ServerRouterMessage {

    private final ApiRequestWithActor apiRequestWithActor;

    private ServerRouterMessage(ApiRequestWithActor apiRequestWithActor) {
        this.apiRequestWithActor = apiRequestWithActor;
    }

    public static ServerRouterMessage newServerRouterMessage(ApiRequestWithActor apiRequestWithActor) {
        return new ServerRouterMessage(apiRequestWithActor);
    }


    public ApiRequestWithActor getApiRequestWithActor() {
        return apiRequestWithActor;
    }


}
