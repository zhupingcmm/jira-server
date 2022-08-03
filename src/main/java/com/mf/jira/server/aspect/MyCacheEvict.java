package com.mf.jira.server.aspect;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface MyCacheEvict {
    String cacheNames() default "";

    String key() default "";

    boolean beforeInvocation() default false;

    boolean allEntries() default false;
}
