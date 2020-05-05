package com.baker.learning.bigdatahbase.model;

import lombok.Data;

/**
 * @description:
 * @create: 2018-11-29 15:21
 * @Version: 1.0
 **/
@Data
public class RespVO {
    private Integer code;
    private String message;
    private Object data;

    public RespVO() {
    }

    public RespVO setSuccess(Object data) {
        this.code = 0;
        this.message = "OK";
        this.data = data;
        return this;
    }

    public RespVO setFailure(String message) {
        this.code = 0;
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        return "RespVO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
