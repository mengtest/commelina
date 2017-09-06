package com.framework.message;

import com.google.common.base.Preconditions;

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

    public int getAsInt() {
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

    public boolean getAsBool() {
        Preconditions.checkArgument(type == DATA_TYPE.BOOL);
        if (this.arg == null) {
            return false;
        }
        return Boolean.valueOf(this.arg.toString());
    }

    public long getAsLong() {
        Preconditions.checkArgument(type == DATA_TYPE.LONG);
        if (this.arg == null) {
            return 0;
        }
        return Long.valueOf(this.arg.toString());
    }

    public double getAsDouble() {
        Preconditions.checkArgument(type == DATA_TYPE.DOUBLE);
        if (this.arg == null) {
            return 0.0;
        }
        return Double.valueOf(this.arg.toString());
    }

    public float getAsFloat() {
        Preconditions.checkArgument(type == DATA_TYPE.FLOAT);
        if (this.arg == null) {
            return 0;
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

}
