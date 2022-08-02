package com.mf.jira.server.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MyCachePutAspect {

    private final RedisTemplate<String, Object> redisTemplate;


    @Around("@annotation(myCachePut)")
    public Object around(ProceedingJoinPoint joinPoint, MyCachePut myCachePut) throws Throwable {
        //获取cache key
        String cacheKey = CacheUtil.getCacheKey(myCachePut.cacheNames(), myCachePut.key(), joinPoint);
        // 执行业务
        Object cacheValue = joinPoint.proceed();
        // 更新缓存
        redisTemplate.opsForValue().set(cacheKey, cacheValue);
        log.info("update cacheKey: {}, cacheVale {}", cacheKey, cacheValue);
        return cacheValue;
    }
}
