package com.baker.learning.bigdataapi.model.result;

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

    public void setSuccess(Object data) {
        this.code = 0;
        this.message = "OK";
        this.data = data;
    }

    public void setFailure(String message) {
        this.code = 0;
        this.message = message;
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
