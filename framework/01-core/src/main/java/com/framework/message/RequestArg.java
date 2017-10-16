package com.framework.message;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
@Deprecated
public class RequestArg {

    private String arg;
    private DATA_TYPE type;

    public RequestArg(String arg, DATA_TYPE type) {
        this.arg = arg;
        this.type = type;
    }

    public Integer getAsInt() {
        Preconditions.checkArgument(type == DATA_TYPE.INT);
        if (Strings.isNullOrEmpty(arg)) {
            return 0;
        }
        return Integer.valueOf(arg);
    }

    public String getAsString() {
        Preconditions.checkArgument(type == DATA_TYPE.STRING);
        return arg;
    }

    public Boolean getAsBool() {
        Preconditions.checkArgument(type == DATA_TYPE.BOOL);
        if (Strings.isNullOrEmpty(arg)) {
            return false;
        }
        return Boolean.valueOf(arg);
    }

    public Long getAsLong() {
        Preconditions.checkArgument(type == DATA_TYPE.LONG);
        if (Strings.isNullOrEmpty(arg)) {
            return 0L;
        }
        return Long.valueOf(arg);
    }

    public Double getAsDouble() {
        Preconditions.checkArgument(type == DATA_TYPE.DOUBLE);
        if (Strings.isNullOrEmpty(arg)) {
            return 0D;
        }
        return Double.valueOf(arg);
    }

    public Float getAsFloat() {
        Preconditions.checkArgument(type == DATA_TYPE.FLOAT);
        if (Strings.isNullOrEmpty(arg)) {
            return 0F;
        }
        return Float.valueOf(arg);
    }
//    public List<Long> getLongArgs() {
//        return getArgs().stream().map(RequestArg::getAsLong).collect(Collectors.toList());
//    }


    public enum DATA_TYPE {
        INT,
        STRING,
        BOOL,
        LONG,
        DOUBLE,
        FLOAT
    }

    public static List<RequestArg>  asList(long[] args) {
        List<RequestArg> argList = Lists.newArrayList();
        for (Long userId : args) {
            argList.add(new RequestArg(userId.toString(), RequestArg.DATA_TYPE.LONG));
        }
        return argList;
    }

}
