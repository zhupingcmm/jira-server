package com.mf.jira.server.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MyCachePut {
    String cacheNames() default "";
    String key() default "";
}
