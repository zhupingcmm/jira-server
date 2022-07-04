package com.mf.jira.server.base;

import lombok.Data;

@Data
public class BaseResponse<T> extends BaseBean{

    private int code;

    private String message;

    private T data;

    public BaseResponse(ResponseEnum responseEnum){
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public static <T> BaseResponse<T> success (T data) {
        BaseResponse<T> response = new BaseResponse<>(ResponseEnum.SUCCESS);
        response.setData(data);
        return response;
    }

    public static <T> BaseResponse<T> error(T data) {
        BaseResponse<T> response = new BaseResponse<>(ResponseEnum.ERROR);
        response.setData(data);
        return response;
    }

    public static <T> BaseResponse<T> error(ResponseEnum responseEnum){
        BaseResponse<T> response = new BaseResponse<>(responseEnum);
        return response;
    }

    public static <T> BaseResponse<T> success(){
        return new BaseResponse<>(ResponseEnum.SUCCESS);
    }


}
