package com.mf.jira.server.base;

import lombok.Getter;
public enum IDTypeEnum {
    JIRA(1, "jira-server");
    IDTypeEnum(int code, String redisCounter) {
        this.code = code;
        this.redisCounter = redisCounter;
    }
    @Getter
    private int code;
    @Getter
    private String redisCounter;
}
