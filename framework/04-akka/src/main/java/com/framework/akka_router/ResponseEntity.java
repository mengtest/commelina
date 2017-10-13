package com.framework.akka_router;

import com.google.protobuf.Internal;

/**
 * Created by @panyao on 2017/10/13.
 */
public class ResponseEntity {

    private final int businessCode;
    private final Object data;

    private static final ResponseEntity EMPTY_SUCCESS = new ResponseEntity(0, null);

    private ResponseEntity(int businessCode, Object data) {
        this.businessCode = businessCode;
        this.data = data;
    }

    public static ResponseEntity error(Internal.EnumLite code) {
        return success(code, null);
    }

    public static ResponseEntity success() {
        return EMPTY_SUCCESS;
    }

    public static ResponseEntity success(Object data) {
        return new ResponseEntity(0, data);
    }

    public static ResponseEntity success(Internal.EnumLite code, Object data) {
        return new ResponseEntity(code.getNumber(), data);
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public Object getData() {
        return data;
    }

    public boolean isSuccess() {
        return businessCode == 0;
    }

}
