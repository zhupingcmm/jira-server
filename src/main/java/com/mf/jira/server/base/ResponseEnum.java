package com.mf.jira.server.base;


import lombok.Getter;


public enum ResponseEnum {
    SUCCESS(0, "请求成功"),
    TOO_MANY_ROWS_AFFECTED(1001,"更新到其他数据"),
    NO_ROWS_AFFECTED(1002, "数据未更新成功"),
    ENTITY_NOT_FOUND(1003, "数据不存在"),
    TRANSFORM_EXCEPTION(1004, "对象转换异常"),
    TOKEN_EXPIRED(1005, "token 过期"),

    NAME_OR_PASSWORD_NOT_VALID(1006, "用户名或者密码不对"),
    ERROR(1007, "系统错误"),


    PROJECT_MODIFY_SUCCESS(1008, "Project 修改成功"),

    PROJECT_DELETE_SUCCESS(1009, "Project 删除成功"),
    SYSTEM_BUSY(1010, "系统繁忙"),
    TOKEN_INVALID(1011, "无效 token");
    @Getter
    private int code;
    @Getter
    private String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }


}
