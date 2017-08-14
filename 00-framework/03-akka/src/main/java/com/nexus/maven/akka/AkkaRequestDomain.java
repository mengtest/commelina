package com.nexus.maven.akka;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Created by @panyao on 2017/8/14.
 */
public final class AkkaRequestDomain {

    private String typeRouter;
    private Object[] args;

    private AkkaRequestDomain() {

    }

    public static AkkaRequestDomain newDomain(String typeRouter, Object... args) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(typeRouter));
        AkkaRequestDomain domain = new AkkaRequestDomain();
        domain.typeRouter = typeRouter;
        domain.args = args;
        return domain;
    }

    public String getTypeRouter() {
        return typeRouter;
    }

    public Object[] getArgs() {
        return args;
    }

}
