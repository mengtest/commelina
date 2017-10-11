package com.framework.message;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/9/15.
 */
public class ApiRequestForward {

    private final Internal.EnumLite opcode;
    private final String version;
    private final RequestArg[] args;

    public ApiRequestForward(Internal.EnumLite opcode, String version, RequestArg[] args) {
        this.opcode = opcode;
        this.version = version;
        this.args = args;
    }

    public Internal.EnumLite getOpcode() {
        return opcode;
    }

    public String getVersion() {
        return this.version;
    }

    public RequestArg[] getArgs() {
        return this.args;
    }

    public long[] getLongArgs() {
        long[] args = new long[getArgs().length];
        for (int i = 0; i < getArgs().length; i++) {
            args[i] = getArgs()[i].getAsLong();
        }
        return args;
    }

    public int[] getIntArgs() {
        int[] args = new int[getArgs().length];
        for (int i = 0; i < getArgs().length; i++) {
            args[i] = getArgs()[i].getAsInt();
        }
        return args;
    }

    public String[] getStringArgs() {
        String[] args = new String[getArgs().length];
        for (int i = 0; i < getArgs().length; i++) {
            args[i] = getArgs()[i].getAsString();
        }
        return args;
    }

    public RequestArg getArg(int argName) {
        try {
            return args[argName];
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }

    public RequestArg[] subArg(int startSite) {
        RequestArg[] args = new RequestArg[getArgs().length - startSite];
        for (int i = startSite; i < getArgs().length; i++) {
            args[i] = getArgs()[i];
        }
        return args;
    }

}
