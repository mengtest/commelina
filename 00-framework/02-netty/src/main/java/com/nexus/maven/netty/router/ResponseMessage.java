package com.nexus.maven.netty.router;

import java.io.Serializable;

/**
 * Created by @panyao on 2016/8/24.
 *
 * @author panyao
 * @coding.net https://coding.net/u/pandaxia
 * @github https://github.com/freedompy
 */
public final class ResponseMessage implements Serializable {

    private transient final Object data;
    private transient int code = 0;

    private PipelineFuture callableHandler;

    private static final ResponseMessage _emptyResponseMessage =
            new ResponseMessage(null);

    private ResponseMessage(Object data) {
        this.data = data;
    }

    public static ResponseMessage okEmpty() {
        return _emptyResponseMessage;
    }

    public static ResponseMessage error(int code) {
        // 保存之后不能使用该方法产地消息，如果需要设置body值，需要使用 messageOk()
        ResponseMessage responseMessage = new ResponseMessage(null);
        responseMessage.code = code;
        return responseMessage;
    }

    public static ResponseMessage success(Object _data) {
        return new ResponseMessage(_data);
    }

    public static ResponseMessage success(int code, Object _data) {
        ResponseMessage responseMessage = success(_data);
        responseMessage.code = code;
        return responseMessage;
    }

    public void addListener(PipelineFuture future) {
        this.callableHandler = future;
    }

    public PipelineFuture getCallableHandler() {
        return this.callableHandler;
    }

    public Object getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}