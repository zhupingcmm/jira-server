package com.mf.jira.server.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Getter
    @Setter
    private Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        private String jwtId = "jira-server";
        private String authorities = "authorities";
        private Long tokenExpireTime = 60*1000L;
    }
}
