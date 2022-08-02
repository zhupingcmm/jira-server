package com.mf.jira.server.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@RequiredArgsConstructor
@Slf4j
@Component
public class MyCacheableAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(myCacheable)")
    public Object doAround(ProceedingJoinPoint joinPoint, MyCacheable myCacheable) throws Throwable {

        String cacheKey = CacheUtil.getCacheKey(myCacheable.cacheNames(), myCacheable.key(), joinPoint);
        Object cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if (cacheValue != null) {
            log.info("cacheKey: {}, cacheValue: {}", cacheKey, cacheValue);
            return cacheValue;
        }

        cacheValue = joinPoint.proceed();

        if (myCacheable.expireInSeconds() <= 0) {
            redisTemplate.opsForValue().set(cacheKey, cacheValue);
        } else {
            redisTemplate.opsForValue().set(cacheKey, cacheValue, myCacheable.expireInSeconds(), TimeUnit.SECONDS);
        }

        return cacheValue ;
    }
}
