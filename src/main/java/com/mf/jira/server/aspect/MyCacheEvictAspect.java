package com.mf.jira.server.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class MyCacheEvictAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Around("@annotation(myCacheEvict)")
    public Object around(ProceedingJoinPoint joinPoint, MyCacheEvict myCacheEvict) throws Throwable {

        String cacheKey = CacheUtil.getCacheKey(myCacheEvict.cacheNames(), myCacheEvict.key(), joinPoint);

        boolean beforeInvocation = myCacheEvict.beforeInvocation();
        boolean allEntries = myCacheEvict.allEntries();

        Object cacheValue = null;
        if (beforeInvocation) {
            deleteCache(allEntries, cacheKey);
            cacheValue = joinPoint.proceed();
        } else {
            cacheValue = joinPoint.proceed();
            deleteCache(allEntries, cacheKey);
        }

        return cacheValue;
    }

    private void deleteCache(boolean allEntries, String cacheKey) {
        if (allEntries) {
            val key = redisTemplate.keys("*");
            assert key != null;
            log.info("delete all cache");
            redisTemplate.delete(key);
        } else {
            log.info("delete cacheKey {}", cacheKey);
            redisTemplate.delete(cacheKey);
        }
    }
}
