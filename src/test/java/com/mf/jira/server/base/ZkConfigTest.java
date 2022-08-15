package com.mf.jira.server.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZkConfigTest {

    private final String PATH = "/config";
    @Autowired
    private ZkConfig zkConfig;

    @Test
    public void testSetConfigData () {
        zkConfig.setConfigData(PATH, "backend:123,12,34");
    }

    @Test
    public void testGetConfigData () throws InterruptedException {
        zkConfig.getConfigData(PATH);
    }
}
