package com.framework.message;

import com.google.protobuf.Internal;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @panyao on 2017/9/15.
 */
public class ApiRequestForward implements Serializable {

    private final Internal.EnumLite opcode;
    private final String version;
    private final List<RequestArg> args;

    public ApiRequestForward(Internal.EnumLite opcode, String version, List<RequestArg> args) {
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

    public List<RequestArg> getArgs() {
        return this.args;
    }

    public List<Long> getLongArgs() {
        return getArgs().stream().map(RequestArg::getAsLong).collect(Collectors.toList());
    }
}
