package com.mf.jira.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JiraServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiraServerApplication.class, args);
    }

}
