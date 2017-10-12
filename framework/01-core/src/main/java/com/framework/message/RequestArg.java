package com.framework.message;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by @panyao on 2017/8/25.
 */
public class RequestArg {

    private Object arg;
    private DATA_TYPE type;

    public RequestArg(Object arg, DATA_TYPE type) {
        this.arg = arg;
        this.type = type;
    }

    public RequestArg(String arg, DATA_TYPE type) {
        this.arg = arg;
        this.type = type;
    }

    public Integer getAsInt() {
        Preconditions.checkArgument(type == DATA_TYPE.INT);
        if (this.arg == null) {
            return 0;
        }
        return Integer.valueOf(this.arg.toString());
    }

    public String getAsString() {
        Preconditions.checkArgument(type == DATA_TYPE.STRING);
        return this.arg != null ? this.arg.toString() : null;
    }

    public Boolean getAsBool() {
        Preconditions.checkArgument(type == DATA_TYPE.BOOL);
        if (this.arg == null) {
            return false;
        }
        return Boolean.valueOf(this.arg.toString());
    }

    public Long getAsLong() {
        Preconditions.checkArgument(type == DATA_TYPE.LONG);
        if (this.arg == null) {
            return 0L;
        }
        return Long.valueOf(this.arg.toString());
    }

    public Double getAsDouble() {
        Preconditions.checkArgument(type == DATA_TYPE.DOUBLE);
        if (this.arg == null) {
            return 0D;
        }
        return Double.valueOf(this.arg.toString());
    }

    public Float getAsFloat() {
        Preconditions.checkArgument(type == DATA_TYPE.FLOAT);
        if (this.arg == null) {
            return 0F;
        }
        return Float.valueOf(this.arg.toString());
    }

    public enum DATA_TYPE {
        INT,
        STRING,
        BOOL,
        LONG,
        DOUBLE,
        FLOAT,
        ARRAY
    }

    public static List<RequestArg>  asList(long[] args) {
        List<RequestArg> argList = Lists.newArrayList();
        for (long userId : args) {
            argList.add(new RequestArg(userId, RequestArg.DATA_TYPE.LONG));
        }
        return argList;
    }

}
