package com.wj.camera.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * FileName: ResponseDevice
 * Author: xiongxiang
 * Date: 2021/1/5
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class BaseDeviceResponse<T> implements Serializable {
    /**
     * code : 200
     * msg : 操作成功!
     */

    @JsonProperty("code")
    private int code;
    @JsonProperty("msg")
    private String msg;

    @JsonProperty("data")
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
