package com.mf.jira.server.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MyCacheEvict {
    String cacheNames() default "";

    String key() default "";

    boolean beforeInvocation() default false;

    boolean allEntries() default false;
}
