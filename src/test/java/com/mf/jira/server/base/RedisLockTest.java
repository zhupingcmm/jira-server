package com.mf.jira.server.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisLockTest {

    @Autowired
    private RedisLock redisLock;

    @Test
    public void testRedisLock(){
        int size = 1;
//        CountDownLatch latch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            int finalI = i;
            new Thread(() -> {
//                try {
//                    latch.await();
                    redisLock.lock();

                    System.out.println("执行了任务" + finalI);

                    redisLock.unlock();

//
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

            }).start();

//            latch.countDown();
        }
    }
}
