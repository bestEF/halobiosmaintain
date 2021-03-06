package com.ltmap.halobiosmaintain.common.result;

import com.google.common.base.MoreObjects;

/**
 * 统一返回结果集
 * @author fjh
 * @date 2020/11/12 12:13
 */
public class Response<T> {

    private boolean success;
    private T result;
    private String error;
    private String code;

    public Response() {
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.success = true;
        this.code = "200";
        this.result = result;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.success = false;
        this.error = error;
        this.code = "-1";
    }

    @Override
    public  String toString() {
        return MoreObjects.toStringHelper(this).add("success", this.success).add("result", this.result).add("error", this.error).omitNullValues().toString();
    }

    public static <T> Response<T> ok(T data) {
        Response<T> resp = new Response();
        resp.setResult(data);
        return resp;
    }

    public static <T> Response<T> ok() {
        return ok((T) null);
    }

    public static <T> Response<T> fail(String error) {
        Response<T> resp = new Response();
        resp.setError(error);
        return resp;
    }

    public static <T> Response<T> fail(String code, String error) {
        Response<T> resp = new Response();
        resp.setError(error);
        resp.setCode(code);
        return resp;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
