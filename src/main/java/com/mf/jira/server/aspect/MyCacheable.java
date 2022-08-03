package com.mf.jira.server.aspect;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MyCacheable {
    /**
     *
     * 缓存的名称前缀，完整的缓存名称生成规则: {cacheName}:{key}
     */
    String cacheNames();

    /**
     *
     * 缓存的键值
     */
    String key();

    /**
     * 缓存过期时间，单位为秒，默认不设置过期时间
     *
     */
    int expireInSeconds() default 0;

    /**
     *限流器获取令牌等待超时时间
     */
    int waitInSeconds() default 0;
}
