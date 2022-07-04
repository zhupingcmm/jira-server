package com.mf.jira.server.base;


import lombok.Getter;


public enum ResponseEnum {
    SUCCESS(0, "请求成功"),
    TOO_MANY_ROWS_AFFECTED(1001,"更新到其他数据"),
    NO_ROWS_AFFECTED(1000, "数据未更新成功"),
    ENTITY_NOT_FOUND(1000, "数据不存在"),
    TRANSFORM_EXCEPTION(9998, "对象转换异常"),
    TOKEN_EXPIRED(8899, "token 过期"),

    NAME_OR_PASSWORD_NOT_VALID(8907, "用户名或者密码不对"),
    ERROR(9999, "系统错误");
    @Getter
    private int code;
    @Getter
    private String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
