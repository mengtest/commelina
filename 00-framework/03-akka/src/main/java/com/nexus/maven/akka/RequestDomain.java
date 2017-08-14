package com.nexus.maven.akka;

import com.google.common.base.Preconditions;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class RequestDomain {

    private int typeRouter;
    private Object[] args;

    private RequestDomain() {

    }

    public static RequestDomain newDomain(int typeRouter, Object... args) {
        Preconditions.checkArgument(typeRouter >= 0);
        RequestDomain domain = new RequestDomain();
        domain.typeRouter = typeRouter;
        domain.args = args;
        return domain;
    }

    public int getTypeRouter() {
        return typeRouter;
    }

    public Object[] getArgs() {
        return args;
    }

}
