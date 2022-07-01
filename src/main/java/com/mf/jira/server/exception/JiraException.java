package com.mf.jira.server.exception;

import com.mf.jira.server.base.ResponseEnum;
import lombok.Getter;


@Getter
public class JiraException extends RuntimeException{
    private int code;
    private String message;
    private ResponseEnum responseEnum;

    public JiraException(int code, String message){
        this.code = code;
        this.message = message;
    }

    public JiraException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.responseEnum = responseEnum;
    }

}
