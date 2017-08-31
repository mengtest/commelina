package com.framework.core;

import com.google.common.base.Preconditions;
import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2016/8/24.
 *
 * @author panyao
 * @coding.net https://coding.net/u/pandaxia
 * @github https://github.com/freedompy
 */
public class BusinessMessage {

    private final int businessCode;
    private final Object data;

    static final String DEFAULT_DATA = null;
    static final int DEFAULT_SUCCESS = 0;

    private BusinessMessage(int businessCode, Object data) {
        this.businessCode = businessCode;
        this.data = data;
    }

    public static BusinessMessage error(Internal.EnumLite code) {
        Preconditions.checkArgument(code.getNumber() > 0);
        return success(code, DEFAULT_DATA);
    }

    public static BusinessMessage success() {
        return new BusinessMessage(DEFAULT_SUCCESS, DEFAULT_DATA);
    }

    public static BusinessMessage success(Object data) {
        return new BusinessMessage(DEFAULT_SUCCESS, data);
    }

    public static BusinessMessage success(Internal.EnumLite code, Object data) {
        return new BusinessMessage(code.getNumber(), data);
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public Object getData() {
        return data;
    }

}