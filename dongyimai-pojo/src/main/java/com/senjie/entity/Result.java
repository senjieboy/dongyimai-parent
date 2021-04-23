package com.senjie.entity;

import java.io.Serializable;

/**
 * @Author SenJie
 * @Data 2021/3/31 11:12
 */
public class Result implements Serializable {
    private  boolean success;
    private String  message;

    public Result() {
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
