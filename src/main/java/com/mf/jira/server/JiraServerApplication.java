package com.mf.jira.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableKafka
@EnableCaching
@EnableAspectJAutoProxy
public class JiraServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JiraServerApplication.class, args);
    }

}
