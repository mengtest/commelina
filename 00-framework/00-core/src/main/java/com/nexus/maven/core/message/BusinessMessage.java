package com.nexus.maven.core.message;

import java.io.Serializable;

/**
 * Created by @panyao on 2016/8/24.
 *
 * @author panyao
 * @coding.net https://coding.net/u/pandaxia
 * @github https://github.com/freedompy
 */
public class BusinessMessage implements Serializable {

    private int businessCode;
    private DataEntity data;

    public static final String DEFAULT_DATA = null;
    public static final int DEFAULT_SUCCESS = 0;

    private BusinessMessage(int businessCode, DataEntity data) {
        this.businessCode = businessCode;
        this.data = data;
    }

    public static BusinessMessage error(int code) {
        return success(code, DEFAULT_DATA);
    }

    public static BusinessMessage success() {
        return success(DEFAULT_SUCCESS, DEFAULT_DATA);
    }

    public static BusinessMessage success(Object data) {
        return success(DEFAULT_SUCCESS, data);
    }

    public static BusinessMessage success(int code, Object data) {
        DataEntity dataEntity = new DataEntity();
        dataEntity.data = data;
        return new BusinessMessage(code, dataEntity);
    }

    public static final class DataEntity {
        Object data;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    public DataEntity getData() {
        return data;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }
}