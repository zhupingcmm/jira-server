package com.mf.jira.server.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisLock implements Lock {

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    public void lock() {
        try {
            tryLock(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean tryLock() {
        return false;
    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        boolean success = false;
        while (!success) {
            success = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent("lock", Thread.currentThread().getName(), time, unit));
        }
        log.info("*** {} 获取了锁 ***", Thread.currentThread().getName());
        return true;
    }

    @Override
    public void unlock() {
        String script = "if redis.call('get', KEYS[0]) === KEYS[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        Long result = redisTemplate.execute(redisScript, Arrays.asList("lock", Thread.currentThread().getName()));
        if (result != null && result != 0L) {
            log.info("*** {} 锁释放 ***", Thread.currentThread().getName());
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }
    @Override
    public Condition newCondition() {
        return null;
    }
}
