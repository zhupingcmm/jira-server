package com.mf.jira.server.aspect;

import com.google.common.hash.BloomFilter;
import com.google.common.util.concurrent.RateLimiter;
import com.mf.jira.server.base.ResponseEnum;
import com.mf.jira.server.exception.JiraException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Aspect
@RequiredArgsConstructor
@Slf4j
@Component
@ConfigurationProperties(prefix = "mycacheable.rate.limit")
public class MyCacheableAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Setter
    private HashMap<String, Double> map;

    private final HashMap<String, RateLimiter> rateLimiters = new HashMap<>();

    @PostConstruct
    public void initRateLimiter() {
        if (!map.keySet().isEmpty()) {
            map.forEach((methodName, permits) -> {
                rateLimiters.put(methodName, RateLimiter.create(permits));
            });
        }

    }

    @Around("@annotation(myCacheable)")
    public Object doAround(ProceedingJoinPoint joinPoint, MyCacheable myCacheable) throws Throwable {

        //限流
        this.rateLimit(joinPoint, myCacheable);

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

    private void rateLimit(ProceedingJoinPoint joinPoint, MyCacheable myCacheable) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        RateLimiter rateLimiter = rateLimiters.get(methodName);
        int waitInSeconds = myCacheable.waitInSeconds();
        if (waitInSeconds <= 0) {
            rateLimiter.acquire();
        } else {
            boolean acquire = rateLimiter.tryAcquire(waitInSeconds, TimeUnit.SECONDS);
            if (!acquire) {
                throw new JiraException(ResponseEnum.SYSTEM_BUSY);
            }
        }
    }
}
