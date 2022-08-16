package com.mf.jira.server.config;

import com.mf.jira.server.base.IDTypeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZkIdGeneratorTest {

    @Autowired
    private ZkIdGenerator zkIdGenerator;

    @Test
    public void testZkIdGenerator() {
        int size = 100;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    long id = zkIdGenerator.getId(IDTypeEnum.JIRA);
                    System.out.println(id);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }).start();

            countDownLatch.countDown();
        }
    }
}
